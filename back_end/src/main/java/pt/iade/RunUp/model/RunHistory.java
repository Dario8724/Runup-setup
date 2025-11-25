package pt.iade.RunUp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "RunHistory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "route_id")
    private Long rotaId;

    private LocalDate dataCorrida;

    private Double distanciaKm;

    private Double duracaoMinutos;

    private String tipo; 

    private Integer calorias;
}
