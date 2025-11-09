package pt.iade.RunUp.model.dto;

import java.util.List;
import java.util.Map;

public class DirectionsResult {
    private double distance; // em metros
    private double duration; // em segundos
    private String polyline;

    public static DirectionsResult fromMap(Map<String, Object> map) {
        DirectionsResult result = new DirectionsResult();

        List<Map<String, Object>> routes = (List<Map<String, Object>>) map.get("routes");
        if (routes == null || routes.isEmpty()) return result;

        Map<String, Object> firstRoute = routes.get(0);
        result.polyline = ((Map<String, String>) firstRoute.get("overview_polyline")).get("points");

        List<Map<String, Object>> legs = (List<Map<String, Object>>) firstRoute.get("legs");
        if (legs != null && !legs.isEmpty()) {
            Map<String, Object> firstLeg = legs.get(0);
            Map<String, Object> distanceObj = (Map<String, Object>) firstLeg.get("distance");
            Map<String, Object> durationObj = (Map<String, Object>) firstLeg.get("duration");

            result.distance = ((Number) distanceObj.get("value")).doubleValue();
            result.duration = ((Number) durationObj.get("value")).doubleValue();
        }

        return result;
    }

    public double getDistance() { return distance; }
    public double getDuration() { return duration; }
    public String getPolyline() { return polyline; }
}
