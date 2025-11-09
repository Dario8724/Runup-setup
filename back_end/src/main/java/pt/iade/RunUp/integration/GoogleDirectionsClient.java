package pt.iade.RunUp.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class GoogleDirectionsClient {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getRoute(double originLat, double originLng,
                                        double destLat, double destLng) {

        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/directions/json")
                .queryParam("origin", originLat + "," + originLng)
                .queryParam("destination", destLat + "," + destLng)
                .queryParam("mode", "walking")
                .queryParam("alternatives", "true")
                .queryParam("key", apiKey)
                .toUriString();

        // ✅ imprime a URL depois que ela é construída
        System.out.println("URL chamada: " + url);

        return restTemplate.getForObject(url, Map.class);
    }
}
