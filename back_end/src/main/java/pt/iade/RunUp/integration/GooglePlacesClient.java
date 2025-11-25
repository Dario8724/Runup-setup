package pt.iade.RunUp.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class GooglePlacesClient {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Logger logger = Logger.getLogger(GooglePlacesClient.class.getName());

    /**
     * Busca localizações próximas usando Nearby Search.
     * keyword: "beach" ou "park" ou qualquer outro (vai mapear para um "type" razoável).
     */
    public Map<String, Object> searchPlaces(double lat, double lng, String keyword) {
        String type;
        if (keyword == null) keyword = "";
        if (keyword.equalsIgnoreCase("beach")) {
            // beach não tem um type exato, usa "natural_feature" como proxy
            type = "natural_feature";
        } else if (keyword.equalsIgnoreCase("park")) {
            type = "park";
        } else {
            type = "point_of_interest";
        }

        try {
            URI uri = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                    .queryParam("location", String.format("%f,%f", lat, lng))
                    .queryParam("radius", 1500)         // 1.5 km radius — pode ajustar
                    .queryParam("type", type)
                    .queryParam("key", apiKey)
                    .build()
                    .encode()   // importante para caracteres especiais
                    .toUri();

            logger.fine("Calling Places API: " + uri.toString());
            Map<String, Object> resp = restTemplate.getForObject(uri, Map.class);
            return resp;
        } catch (Exception e) {
            logger.severe("Erro na chamada Google Places: " + e.getMessage());
            return null;
        }
    }
}
