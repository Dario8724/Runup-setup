package pt.iade.RunUp.model.dto;

import lombok.*;
import lombok.Builder;

@Data
@Builder
public class RouteResponse {
    private Long id;
    private String name;
    private double distance;
    private double duration;    
    private String polyline;
}
