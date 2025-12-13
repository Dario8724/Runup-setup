package pt.iade.RunUp.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class GoogleElevationClient {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate rest = new RestTemplate();
    private final Logger logger = Logger.getLogger(GoogleElevationClient.class.getName());

    public List<Map<String, Object>> getElevations(List<String> latLngPairs) {
        if (latLngPairs == null || latLngPairs.isEmpty())
            return List.of();

        try {
            int batchSize = 512;
            int total = latLngPairs.size();
            List<Map<String, Object>> allResults = new java.util.ArrayList<>();

            for (int i = 0; i < total; i += batchSize) {

                int end = Math.min(i + batchSize, total);
                String locations = String.join("|", latLngPairs.subList(i, end));

                // === IMPORTANTE: construir URL manual para NÃO ESCAPAR "|" ===
                String url =
                        "https://maps.googleapis.com/maps/api/elevation/json" +
                                "?locations=" + locations +
                                "&key=" + apiKey;

                logger.info("DEBUG ELEVATION URL: " + url);

                Map<String, Object> resp = rest.getForObject(url, Map.class);

                if (resp == null) {
                    logger.warning("Google Elevation retornou null.");
                    continue;
                }

                if (!"OK".equals(resp.get("status"))) {
                    logger.warning("Google Elevation API ERRO: " + resp.get("status"));
                    if (resp.containsKey("error_message"))
                        logger.warning("MSG: " + resp.get("error_message"));
                    continue;
                }

                Object results = resp.get("results");
                if (results instanceof List<?> list) {
                    allResults.addAll((List<Map<String, Object>>) list);
                }
            }

            return allResults;

        } catch (Exception e) {
            logger.severe("Erro ao consultar Google Elevation API: " + e.getMessage());
            return List.of();
        }
    }
}
