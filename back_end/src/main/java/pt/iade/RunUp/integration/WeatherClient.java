package pt.iade.RunUp.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class WeatherClient {
    
    @Value("${openweather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getWeather(double lat, double lng) {
    String url = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric"
            .formatted(lat, lng, apiKey);

    Map<String, Object> response = restTemplate.getForObject(url, Map.class);
    if (response == null || !response.containsKey("weather"))
        return "Unknown";

    var weatherList = (java.util.List<Map<String, Object>>) response.get("weather");
    return (String) weatherList.get(0).get("main");
}
}