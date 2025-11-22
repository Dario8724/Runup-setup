package pt.iade.RunUp.model.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RunHistoryRequestDTO {

    private Long usuarioId;
    private Long rotaId;
    private LocalDate dataCorrida;
    private Double distanciaKm;
    private Double duracaoMinutos;
    private String tipo;
    private Integer calorias;
}
