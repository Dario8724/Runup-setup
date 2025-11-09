package pt.iade.RunUp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.iade.RunUp.integration.GoogleDirectionsClient;
import pt.iade.RunUp.model.Route;
import pt.iade.RunUp.model.dto.DirectionsResult;
import pt.iade.RunUp.model.dto.RouteRequest;
import pt.iade.RunUp.model.dto.RouteResponse;
import pt.iade.RunUp.repository.RouteRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final GoogleDirectionsClient directionsClient;
    private final RouteRepository routeRepository;
    private final RouteScorer scorer;

    public RouteResponse generateRoute(RouteRequest request) {

        double originLat = request.getOriginLat();
        double originLng = request.getOriginLng();
        double destLat = request.getDestLat();
        double destLng = request.getDestLng();

        // 🔹 Obter todas as rotas do Google
        Map<String, Object> data = directionsClient.getRoute(originLat, originLng, destLat, destLng);
        List<Map<String, Object>> routes = (List<Map<String, Object>>) data.get("routes");
        if (routes == null || routes.isEmpty()) {
            throw new RuntimeException("Nenhuma rota encontrada");
        }

        // 🔹 Avaliar cada rota
        Map<String, Object> bestRoute = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (Map<String, Object> routeData : routes) {
            DirectionsResult directions = DirectionsResult.fromMap(Map.of("routes", List.of(routeData)));

            double score = scorer.scoreRoute(
                directions.getPolyline(),
                request.isPreferTrees(),
                request.isNearBeach(),
                request.isNearPark(),
                request.isSunnyRoute(),
                request.getDesiredDistanceKm()
            );

        if (score > bestScore) {
            bestScore = score;
            bestRoute = routeData;
        }
    }

    // 🔹 Criar objeto final da melhor rota
    DirectionsResult finalResult = DirectionsResult.fromMap(Map.of("routes", List.of(bestRoute)));

    // ✅ Criar entidade rota
    Route rota = new Route();
    rota.setNome(request.getNome().trim());
    rota.setDistanciaKm(finalResult.getDistance() / 1000.0);
    rota.setElevacao(0.0);
    rota.setPontoInicial(originLat + "," + originLng);
    rota.setPontoFinal(destLat + "," + destLng);
    rota.setTipo(request.getTipo()); // corrida | caminhada

    // ✅ Montar filtros
    String filtro = "";
    if (request.isPreferTrees()) filtro += "árvores,";
    if (request.isNearBeach()) filtro += "praia,";
    if (request.isNearPark()) filtro += "parque,";
    if (request.isSunnyRoute()) filtro += "sol,";
    if (!filtro.isEmpty()) filtro = filtro.substring(0, filtro.length() - 1);

    rota.setFiltro(filtro);

    // ✅ Salvar no banco
    routeRepository.save(rota);

    return RouteResponse.builder()
            .id(rota.getId())
            .name(rota.getNome())
            .distance(rota.getDistanciaKm())
            .duration(finalResult.getDuration())
            .polyline(finalResult.getPolyline())
            .build();
    }
}