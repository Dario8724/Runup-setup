package pt.iade.RunUp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "corrida")
@Data
public class Run {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "corrida_id")
    private Long id;

    @Column(name = "corrida_data")
    private String date;

    @Column(name = "corrida_duracao")
    private String duration;

    @Column(name = "corrida_distancia_km")
    private Double distanceKm;

    @Column(name = "corrida_calorias")
    private Integer calories;

    // FK → rota
    @ManyToOne
    @JoinColumn(name = "rota_id")
    private Route route;

    // FK → tipo
    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private RunType type;
}
