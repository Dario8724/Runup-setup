package pt.iade.RunUp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class GoogleDirectionsClient {

    @Value("${google.api.key}")
    private String apiKey;

    private static final String DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/json";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = Logger.getLogger(GoogleDirectionsClient.class.getName());

    /**
     * Chamada básica (origin -> destination).
     */
    public Map<String, Object> getRoute(double originLat, double originLng,
                                        double destLat, double destLng) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(DIRECTIONS_URL)
                    .queryParam("origin", originLat + "," + originLng)
                    .queryParam("destination", destLat + "," + destLng)
                    .queryParam("mode", "walking")
                    .queryParam("alternatives", "true")
                    .queryParam("key", apiKey)
                    .build()
                    .encode()
                    .toUri();

            logger.fine("Calling Directions: " + uri.toString());
            Map<String, Object> resp = restTemplate.getForObject(uri, Map.class);
            return resp;
        } catch (Exception e) {
            logger.severe("Erro na chamada básica de Directions: " + e.getMessage());
            return null;
        }
    }

    /**
     * Chamada com waypoints — ideal para loops (origin == destination).
     * waypoints deve estar no formato "via:lat,lng|via:lat2,lng2|..."
     *
     * Nota: usamos UriComponentsBuilder.encode() para garantir que pipes/commas sejam corretamente codificados.
     */
    public Map<String, Object> getRouteWithWaypoints(double originLat, double originLng,
                                                     double destLat, double destLng,
                                                     String waypointParam) {
        try {
            UriComponentsBuilder b = UriComponentsBuilder.fromHttpUrl(DIRECTIONS_URL)
                    .queryParam("origin", originLat + "," + originLng)
                    .queryParam("destination", destLat + "," + destLng)
                    .queryParam("mode", "walking")
                    .queryParam("alternatives", "true")
                    .queryParam("key", apiKey);

            if (waypointParam != null && !waypointParam.isBlank()) {
                // adiciona waypoints como valor de query; será codificado por .encode()
                b.queryParam("waypoints", waypointParam);
            }

            URI uri = b.build().encode().toUri();
            logger.fine("Calling Directions (waypoints): " + uri.toString());
            Map<String, Object> resp = restTemplate.getForObject(uri, Map.class);
            return resp;
        } catch (Exception e) {
            logger.severe("Erro em Directions com waypoints: " + e.getMessage());
            return null;
        }
    }
}
