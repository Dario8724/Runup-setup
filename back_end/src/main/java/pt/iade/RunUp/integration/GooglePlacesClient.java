package pt.iade.RunUp.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Locale;
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
     *
     * @param lat           latitude de origem
     * @param lng           longitude de origem
     * @param radius        raio em metros (ex: 1500, 5000)
     * @param keywordOrType palavra-chave amigável ("park", "praia", "beach",
     *                      "parque", "tourist_attraction")
     */
    public Map<String, Object> searchNearby(double lat, double lng, int radius, String keywordOrType) {
        String type;

        if (keywordOrType == null) {
            keywordOrType = "";
        }

        String k = keywordOrType.toLowerCase(Locale.ROOT);

        if (k.equals("park") || k.equals("parque")) {
            type = "park";
        } else if (k.equals("praia") || k.equals("beach")) {
            // não existe "beach" como type, usamos algo próximo
            type = "natural_feature"; // ou "tourist_attraction", se preferires
        } else if (k.equals("tourist_attraction")) {
            type = "tourist_attraction";
        } else {
            type = "point_of_interest";
        }

        try {
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                    .queryParam("location", String.format(Locale.US, "%f,%f", lat, lng))
                    .queryParam("radius", radius)
                    .queryParam("type", type)
                    .queryParam("key", apiKey)
                    .build(true) // não mexer em encoding
                    .toUri();

            logger.fine("Calling Places API: " + uri);
            Map<String, Object> resp = restTemplate.getForObject(uri, Map.class);
            return resp;
        } catch (Exception e) {
            logger.severe("Erro na chamada Google Places: " + e.getMessage());
            return null;
        }
    }

    public Map<String, Object> searchPlaces(double lat, double lng, String keyword) {
        return searchNearby(lat, lng, 1500, keyword);
    }
}
