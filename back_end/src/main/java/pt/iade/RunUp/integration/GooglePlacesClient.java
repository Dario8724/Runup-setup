package pt.iade.RunUp.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class GooglePlacesClient {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> searchPlaces(double lat, double lng, String keyword) {
        String url = String.format(
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=300&keyword=%s&key=%s",
            lat, lng, keyword, apiKey
        );

        return restTemplate.getForObject(url, Map.class);
    }
}
