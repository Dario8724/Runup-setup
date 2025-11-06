package pt.iade.RunUp.util;

import java.util.ArrayList;
import java.util.List;

public class PolylineDecoder {
    // returns list of double[]{lat, lng}
    public static List<double[]> decode(String encoded) {
        List<double[]> path = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int result = 0, shift = 0;
            int b;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            result = 0;
            shift = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            double finalLat = lat / 1E5;
            double finalLng = lng / 1E5;
            path.add(new double[]{finalLat, finalLng});
        }
        return path;
    }
}
