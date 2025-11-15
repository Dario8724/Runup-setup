package pt.iade.RunUp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.iade.RunUp.integration.GoogleDirectionsClient;
import pt.iade.RunUp.integration.GooglePlacesClient;
import pt.iade.RunUp.integration.GoogleElevationClient;
import pt.iade.RunUp.model.Route;
import pt.iade.RunUp.model.dto.DirectionsResult;
import pt.iade.RunUp.model.dto.RouteRequest;
import pt.iade.RunUp.model.dto.RouteResponse;
import pt.iade.RunUp.repository.RouteRepository;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final GoogleDirectionsClient directionsClient;
    private final GooglePlacesClient placesClient;
    private final GoogleElevationClient elevationClient;
    private final RouteRepository routeRepository;
    private final RouteScorer scorer;

    private final Logger logger = Logger.getLogger(RouteService.class.getName());

    public RouteResponse generateRoute(RouteRequest request) {

        double originLat = request.getOriginLat();
        double originLng = request.getOriginLng();
        double desiredKm = Math.max(0.5, request.getDesiredDistanceKm());

        // === 1) GERA WAYPOINTS ===
        int numWaypoints = chooseWaypointCountForDistance(desiredKm);
        double radiusMeters = estimateRadiusForDesiredDistance(desiredKm);
        List<double[]> baseWaypoints = generateWaypointsCircle(originLat, originLng, radiusMeters, numWaypoints, 0.20);
        String waypointParam = buildWaypointsParam(baseWaypoints);

        // === 2) CHAMADA DIRECTIONS ===
        Map<String, Object> data = directionsClient.getRouteWithWaypoints(
                originLat, originLng, originLat, originLng, waypointParam
        );

        List<Map<String, Object>> routes = data == null ? null : (List<Map<String, Object>>) data.get("routes");

        // === 3) FALLBACK CASO NÃO HAJA ROTAS ===
        if (routes == null || routes.isEmpty()) {
            logger.warning("Nenhuma rota retornada; tentando fallback simples.");
            Map<String, Object> fallback = directionsClient.getRoute(
                    originLat, originLng, baseWaypoints.get(0)[0], baseWaypoints.get(0)[1]
            );
            routes = fallback == null ? null : (List<Map<String, Object>>) fallback.get("routes");
            if (routes == null || routes.isEmpty())
                throw new RuntimeException("Nenhuma rota encontrada pelo Google Directions.");
        }

        // === 4) ESCOLHE A MELHOR ROTA ===
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

        // === 5) CRIA OBJETO ROUTE ===
        Route rota = new Route();
        rota.setNome(request.getNome() == null ? "Rota" : request.getNome().trim());
        rota.setDistanciaKm(bestDirections.getDistance() / 1000.0);
        rota.setPontoInicial(originLat + "," + originLng);
        rota.setTipo(request.getTipo());

        // Define ponto final
        try {
            List<Map<String, Object>> legs = (List<Map<String, Object>>) bestRouteData.get("legs");
            if (legs != null && !legs.isEmpty()) {
                Map<String, Object> endLoc = (Map<String, Object>) legs.get(legs.size() - 1).get("end_location");
                double endLat = ((Number) endLoc.get("lat")).doubleValue();
                double endLng = ((Number) endLoc.get("lng")).doubleValue();
                rota.setPontoFinal(endLat + "," + endLng);
            } else {
                rota.setPontoFinal(originLat + "," + originLng);
            }
        } catch (Exception e) {
            rota.setPontoFinal(originLat + "," + originLng);
        }

        // === 7) CALCULA ELEVAÇÃO ===
double elevationGain = 0.0;
try {
    List<double[]> coords = decodePolyline(bestDirections.getPolyline());

    // DEBUG 1: quantidade de pontos
    logger.info("DEBUG: Polyline decodificado com " + coords.size() + " pontos.");

    // DEBUG 2: primeiros 5 pontos
    coords.stream().limit(5).forEach(p ->
            logger.info("DEBUG POINT: " + p[0] + "," + p[1])
    );
    List<String> latLngPairs = coords.stream()
            .map(c -> String.format(Locale.US, "%.6f,%.6f", c[0], c[1]))
            .toList();

    // DEBUG 3: tamanho da requisição enviada ao Google
    logger.info("DEBUG: Enviando " + latLngPairs.size() + " pontos para Google Elevation.");


    var elevationResults = elevationClient.getElevations(latLngPairs);

     // DEBUG 4: resposta crua do Google
    logger.info("DEBUG: RESPOSTA RAW DO GOOGLE ELEVATION:");
    if (elevationResults != null) {
        elevationResults.forEach(r -> logger.info("RAW: " + r));
    } else {
        logger.warning("DEBUG: elevationResults veio NULL!");
    }

    double lastElev = -1.0;
    for (var e : elevationResults) {
        double elev = ((Number) e.get("elevation")).doubleValue();
        if (lastElev > 0 && elev > lastElev)
            elevationGain += (elev - lastElev);
        lastElev = elev;
    }

    rota.setElevacao(elevationGain);
    logger.info("Ganho de elevação calculado: " + elevationGain + " m");

} catch (Exception ex) {
    logger.warning("Falha ao calcular elevação: " + ex.getMessage());
    rota.setElevacao(0.0);
}

// === 8) DEFINE FILTROS ===
StringBuilder filtro = new StringBuilder();
if (request.isPreferTrees()) filtro.append("árvores,");
if (request.isNearBeach()) filtro.append("praia,");
if (request.isNearPark()) filtro.append("parque,");
if (request.isSunnyRoute()) filtro.append("sol,");
if (request.isAvoidHills()) filtro.append("evitar subidas,");

String filtroFinal = filtro.toString();
if (filtroFinal.isEmpty()) {
    filtroFinal = "sem filtro";
} else {
    filtroFinal = filtroFinal.substring(0, filtroFinal.length() - 1);
}
rota.setFiltro(filtroFinal);

// === 9) SALVA APENAS UMA VEZ ===
routeRepository.save(rota);


        // === 9) RETORNA RESPOSTA ===
        return RouteResponse.builder()
                .id(rota.getId())
                .name(rota.getNome())
                .distance(rota.getDistanciaKm())
                .duration(bestDirections.getDuration())
                .polyline(bestDirections.getPolyline())
                .build();
    }

    // ==== HELPERS ====

    private int chooseWaypointCountForDistance(double km) {
        if (km <= 2.5) return 3;
        if (km <= 6.0) return 4;
        return 6;
    }

    private double estimateRadiusForDesiredDistance(double desiredKm) {
        double desiredMeters = desiredKm * 1000.0;
        double r = desiredMeters / (2.0 * Math.PI);
        return Math.max(250.0, r * 0.65);
    }

    private List<double[]> generateWaypointsCircle(double originLat, double originLng,
                                                   double radiusMeters, int n, double jitterFactor) {
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

    private String buildWaypointsParam(List<double[]> pts) {
        return pts.stream()
                .map(p -> String.format(Locale.US, "via:%.6f,%.6f", p[0], p[1]))
                .collect(Collectors.joining("|"));
    }

    private double[] destinationFromOriginMeters(double originLat, double originLng,
                                                 double distanceMeters, double bearingDeg) {
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

    private List<double[]> decodePolyline(String encoded) {
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
