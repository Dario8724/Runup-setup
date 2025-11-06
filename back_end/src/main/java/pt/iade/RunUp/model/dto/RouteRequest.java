package pt.iade.RunUp.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteRequest {
    private String nome;
    private double originLat;
    private double originLng;
    private double destLat;
    private double destLng;
    private double desiredDistanceKm;
    private boolean preferTrees;
    private boolean nearBeach;
    private boolean nearPark;
    private boolean sunnyRoute;
    private boolean avoidHills;
    private String tipo; 
}
