package pt.iade.RunUp.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RunHistoryResponseDTO {

    private Long id;
    private String dataCorrida; 
    private String local; 
    private Double distanciaKm;
    private String duracao; 
    private String tipo;
    private Integer calorias;
}
