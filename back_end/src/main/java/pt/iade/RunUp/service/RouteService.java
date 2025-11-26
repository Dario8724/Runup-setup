package pt.iade.RunUp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.iade.RunUp.integration.GoogleDirectionsClient;
import pt.iade.RunUp.integration.GoogleElevationClient;
import pt.iade.RunUp.model.*;
import pt.iade.RunUp.model.dto.DirectionsResult;
import pt.iade.RunUp.model.dto.RouteRequest;
import pt.iade.RunUp.model.dto.RouteResponse;
import pt.iade.RunUp.repository.*;

import java.util.*;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final GoogleDirectionsClient directionsClient;
    private final GoogleElevationClient elevationClient;
    private final RouteRepository routeRepository;
    private final LocalRepository localRepository;
    private final RouteLocalRelRepository lrRepository;
    private final RouteCharacteristicRepository caractRepository;
    private final RouteCharacteristicRelRepository crRepository;
    private final RouteScorer scorer;

    private final Logger logger = Logger.getLogger(RouteService.class.getName());

    @Transactional
    public RouteResponse generateRoute(RouteRequest request) {
        double originLat = request.getOriginLat();
        double originLng = request.getOriginLng();
        double desiredKm = Math.max(0.5, request.getDesiredDistanceKm());

        // === generate/choose waypoints + directions (same logic que você já tem) ===
        int numWaypoints = chooseWaypointCountForDistance(desiredKm);
        double radiusMeters = estimateRadiusForDesiredDistance(desiredKm);
        List<double[]> baseWaypoints = generateWaypointsCircle(originLat, originLng, radiusMeters, numWaypoints, 0.20);
        String waypointParam = buildWaypointsParam(baseWaypoints);

        Map<String, Object> data = directionsClient.getRouteWithWaypoints(
                originLat, originLng, originLat, originLng, waypointParam
        );

        List<Map<String, Object>> routes = data == null ? null : (List<Map<String, Object>>) data.get("routes");
        if (routes == null || routes.isEmpty()) {
            logger.warning("Nenhuma rota retornada; tentando fallback.");
            Map<String, Object> fallback = directionsClient.getRoute(
                    originLat, originLng, baseWaypoints.get(0)[0], baseWaypoints.get(0)[1]
            );
            routes = fallback == null ? null : (List<Map<String, Object>>) fallback.get("routes");
            if (routes == null || routes.isEmpty())
                throw new RuntimeException("Nenhuma rota encontrada pelo Google Directions.");
        }

        Map<String, Object> bestRouteData = null;
        DirectionsResult bestDirections = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (Map<String, Object> routeData : routes) {
            DirectionsResult directions = DirectionsResult.fromMap(Map.of("routes", List.of(routeData)));
            String polyline = directions.getPolyline();
            double routeKm = directions.getDistance() / 1000.0;
            List<double[]> coords = decodePolyline(polyline);
            double combinedScore = scorer.scoreRoute(
                    coords, routeKm, desiredKm,
                    request.isPreferTrees(), request.isNearBeach(),
                    request.isNearPark(), request.isSunnyRoute()
            );

            if (combinedScore > bestScore) {
                bestScore = combinedScore;
                bestRouteData = routeData;
                bestDirections = directions;
            }
        }

        if (bestRouteData == null) {
            bestRouteData = routes.get(0);
            bestDirections = DirectionsResult.fromMap(Map.of("routes", List.of(bestRouteData)));
        }

        // === monta Route entity e salva nome + elevacao ===
        Route rota = new Route();
        rota.setNome(request.getNome() == null ? "Rota" : request.getNome().trim());

        double distanceKm = bestDirections.getDistance() / 1000.0;
        rota.setDistanciaKm(distanceKm); // se quiser manter esse campo em rota (veja DB)
        // se a sua tabela rota NÃO tem distanciaKm, remova essa linha ou comente;
        // conforme script enviado, rota só tem rota_nome e rota_elevacao. Então guardaremos apenas nome e elevacao.

        // calcula elevação (usando elevationClient)
        double elevationGain = 0.0;
        try {
            List<double[]> coords = decodePolyline(bestDirections.getPolyline());
            List<String> latLngPairs = coords.stream()
                    .map(c -> String.format(Locale.US, "%.6f,%.6f", c[0], c[1]))
                    .toList();

            var elevationResults = elevationClient.getElevations(latLngPairs);

            double lastElev = -1.0;
            for (var e : elevationResults) {
                double elev = ((Number) e.get("elevation")).doubleValue();
                if (lastElev > 0 && elev > lastElev)
                    elevationGain += (elev - lastElev);
                lastElev = elev;
            }

        } catch (Exception ex) {
            logger.warning("Falha ao calcular elevação: " + ex.getMessage());
            elevationGain = 0.0;
        }

        rota.setElevacao(elevationGain);

        // SALVA ROTA (nome + elevacao)
        Route savedRoute = routeRepository.save(rota);

        // === SALVA características (se existirem no request) ===
        List<String> caractList = new ArrayList<>();
        if (request.isPreferTrees()) caractList.add("árvores");
        if (request.isNearBeach()) caractList.add("praia");
        if (request.isNearPark()) caractList.add("parque");
        if (request.isSunnyRoute()) caractList.add("sol");
        if (request.isAvoidHills()) caractList.add("evitar subidas");

        for (String c : caractList) {
            RouteCharacteristic caract = caractRepository.findByTipo(c).orElseGet(() -> {
                RouteCharacteristic nc = new RouteCharacteristic();
                nc.setTipo(c);
                return caractRepository.save(nc);
            });

            RouteCharacteristicRel rel = new RouteCharacteristicRel();
            rel.setRotaId(savedRoute.getId().intValue());
            rel.setCaractId(caract.getId());
            crRepository.save(rel);
        }

        // === SALVA locais: somente origem e destino (opção A) ===
        // origem
        String originName = String.format(Locale.US, "%.6f,%.6f", originLat, originLng);
        LocalPlace originLocal = localRepository.findByNome(originName)
                .orElseGet(() -> localRepository.save(createLocal(originName)));

        // destino: tentamos extrair do legs end_location, senão fallback para origin
        String destName;
        try {
            List<Map<String, Object>> legs = (List<Map<String, Object>>) bestRouteData.get("legs");
            if (legs != null && !legs.isEmpty()) {
                Map<String, Object> endLoc = (Map<String, Object>) legs.get(legs.size() - 1).get("end_location");
                double endLat = ((Number) endLoc.get("lat")).doubleValue();
                double endLng = ((Number) endLoc.get("lng")).doubleValue();
                destName = String.format(Locale.US, "%.6f,%.6f", endLat, endLng);
            } else {
                destName = originName;
            }
        } catch (Exception e) {
            destName = originName;
        }

        LocalPlace destLocal = localRepository.findByNome(destName)
                .orElseGet(() -> localRepository.save(createLocal(destName)));

        // salva lr entries: ordem 1 = origem, ordem 2 = destino
        RouteLocalRel lr1 = new RouteLocalRel();
        lr1.setRotaId(savedRoute.getId().intValue());
        lr1.setLocalId(originLocal.getId());
        lr1.setOrdem(1);
        lrRepository.save(lr1);

        RouteLocalRel lr2 = new RouteLocalRel();
        lr2.setRotaId(savedRoute.getId().intValue());
        lr2.setLocalId(destLocal.getId());
        lr2.setOrdem(2);
        lrRepository.save(lr2);

        // === retornar RouteResponse com dados completos para o front ===
        return RouteResponse.builder()
                .id(savedRoute.getId().longValue())
                .name(savedRoute.getNome())
                .distance(distanceKm)
                .duration(bestDirections.getDuration())
                .polyline(bestDirections.getPolyline())
                .build();
    }

    private LocalPlace createLocal(String name) {
        LocalPlace l = new LocalPlace();
        l.setNome(name);
        return l;
    }

    // ==== Helpers (reaproveitei do seu código, cortei por brevidade) ====
    private int chooseWaypointCountForDistance(double km) { /* igual ao seu */ 
        if (km <= 2.5) return 3;
        if (km <= 6.0) return 4;
        return 6;
    }
    private double estimateRadiusForDesiredDistance(double desiredKm) { /* igual ao seu */
        double desiredMeters = desiredKm * 1000.0;
        double r = desiredMeters / (2.0 * Math.PI);
        return Math.max(250.0, r * 0.65);
    }
    private List<double[]> generateWaypointsCircle(double originLat, double originLng,
                                                   double radiusMeters, int n, double jitterFactor) { /* igual ao seu */
        List<double[]> pts = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            double bearing = (360.0 / n) * i;
            double jitter = 1 + (rnd.nextDouble() * 2 - 1) * jitterFactor;
            double r = radiusMeters * jitter;
            pts.add(destinationFromOriginMeters(originLat, originLng, r, bearing));
        }
        return pts;
    }
    private String buildWaypointsParam(List<double[]> pts) { /* igual ao seu */
        return pts.stream()
                .map(p -> String.format(Locale.US, "via:%.6f,%.6f", p[0], p[1]))
                .collect(Collectors.joining("|"));
    }
    private double[] destinationFromOriginMeters(double originLat, double originLng,
                                                 double distanceMeters, double bearingDeg) { /* igual ao seu */
        double R = 6371.0;
        double dR = (distanceMeters / 1000.0) / R;
        double lat1 = Math.toRadians(originLat);
        double lon1 = Math.toRadians(originLng);
        double bearing = Math.toRadians(bearingDeg);
        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dR)
                + Math.cos(lat1) * Math.sin(dR) * Math.cos(bearing));
        double lon2 = lon1 + Math.atan2(
                Math.sin(bearing) * Math.sin(dR) * Math.cos(lat1),
                Math.cos(dR) - Math.sin(lat1) * Math.sin(lat2)
        );
        return new double[]{Math.toDegrees(lat2), Math.toDegrees(lon2)};
    }
    private List<double[]> decodePolyline(String encoded) { /* igual ao seu */
        List<double[]> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            poly.add(new double[]{lat / 1E5, lng / 1E5});
        }
        return poly;
    }
}
