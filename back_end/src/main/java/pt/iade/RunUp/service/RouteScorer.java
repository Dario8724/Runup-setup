package pt.iade.RunUp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.iade.RunUp.integration.GooglePlacesClient;
import pt.iade.RunUp.integration.WeatherClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RouteScorer {
    private final GooglePlacesClient placesClient;
    private final WeatherClient weatherClient;

    // 🔹 Decodifica polyline do Google
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

            double latitude = lat / 1E5;
            double longitude = lng / 1E5;
            poly.add(new double[]{latitude, longitude});
        }
        return poly;
    }

    // 🔹 Ponto central aproximado
    private double[] getPolylineCenter(String encoded) {
        List<double[]> points = decodePolyline(encoded);
        if (points.isEmpty()) return new double[]{0.0, 0.0};
        int mid = points.size() / 2;
        return points.get(mid);
    }

    // 🔹 Avaliação da rota com preferências
    public double scoreRoute(String polyline, boolean preferTrees, boolean nearBeach,
                             boolean nearPark, boolean preferSun, double targetKm) {

        double score = 0.0;

        // Ponto médio da rota
        double[] center = getPolylineCenter(polyline);
        double lat = center[0];
        double lng = center[1];

        // 🌳 Árvores — no Google Places não há tipo “tree”, então usamos “park” como aproximação
        if (preferTrees) {
            Map<String, Object> trees = placesClient.searchPlaces(lat, lng, "park");
            int count = extractPlaceCount(trees);
            score += count * 0.1;
        }

        // 🏖️ Praia
        if (nearBeach) {
            Map<String, Object> beach = placesClient.searchPlaces(lat, lng, "beach");
            int count = extractPlaceCount(beach);
            score += count * 1.0;
        }

        // 🌲 Parque
        if (nearPark) {
            Map<String, Object> park = placesClient.searchPlaces(lat, lng, "park");
            int count = extractPlaceCount(park);
            score += count * 0.8;
        }

        // ☀️ Clima ensolarado
        if (preferSun) {
            boolean sunny = weatherClient.isSunny(lat, lng);
            score += sunny ? 2.0 : -1.0;
        }

        return score;
    }

    // 🔹 Contagem de resultados da API Google Places
    private int extractPlaceCount(Map<String, Object> data) {
        if (data == null || !data.containsKey("results")) return 0;
        List<?> results = (List<?>) data.get("results");
        return results.size();
    }
}
