package pt.iade.RunUp.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pt.iade.RunUp.model.dto.RoutePointDTO;
import pt.iade.RunUp.model.dto.TipoAtividade;
import pt.iade.RunUp.util.PolylineDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GoogleDirectionsClient {

    @Value("${google.api.key}")
    private String apiKey;

    private static final String DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/json";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<RoutePointDTO> gerarPontosDaRota(double startLat,
                                                 double startLng,
                                                 double distanceKm,
                                                 TipoAtividade tipoAtividade) {

        String origin = startLat + "," + startLng;

        double deltaLat = distanceKm / 111.0;
        String destination = (startLat + deltaLat) + "," + startLng;

        String mode = "walking"; 

        String url = UriComponentsBuilder.fromHttpUrl(DIRECTIONS_URL)
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("mode", mode)
                .queryParam("key", apiKey)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        List<RoutePointDTO> pontos = new ArrayList<>();

        if (response == null) {
            return pontos;
        }

        Object status = response.get("status");
        if (status == null || !"OK".equals(status.toString())) {
            return pontos;
        }

        List<Map<String, Object>> routes = (List<Map<String, Object>>) response.get("routes");
        if (routes == null || routes.isEmpty()) {
            return pontos;
        }

        Map<String, Object> route = routes.get(0);
        Map<String, Object> overviewPolyline = (Map<String, Object>) route.get("overview_polyline");
        if (overviewPolyline == null) {
            return pontos;
        }

        String encoded = (String) overviewPolyline.get("points");
        if (encoded == null || encoded.isEmpty()) {
            return pontos;
        }

        List<double[]> decoded = PolylineDecoder.decode(encoded);

        for (double[] coord : decoded) {
            RoutePointDTO dto = new RoutePointDTO();
            dto.setLatitude(coord[0]);
            dto.setLongitude(coord[1]);
            dto.setElevation(0.0); 
            pontos.add(dto);
        }

        return pontos;
    }
}
