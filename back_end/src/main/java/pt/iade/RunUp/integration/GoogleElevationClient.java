package pt.iade.RunUp.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
public class GoogleElevationClient {
    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate rest = new RestTemplate();

    /**
     * pede elevacao para uma lista de pontos (format: "lat,lng|lat,lng")
     */
    public List<Map<String,Object>> getElevations(List<String> latLngPairs) {
        String locations = String.join("|", latLngPairs);
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/elevation/json")
                .queryParam("locations", locations)
                .queryParam("key", apiKey)
                .toUriString();

        Map<String,Object> resp = rest.getForObject(url, Map.class);
        if (resp == null) return List.of();
        Object results = resp.get("results");
        if (results instanceof List) return (List<Map<String,Object>>) results;
        return List.of();
    }
}
