package pt.iade.RunUp.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "corrida")
public class Corrida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "corrida_id")
    private Integer id;

    @Column(name = "corrida_data")
    private LocalDate data;

    @Column(name = "corrida_tempo")
    private LocalTime tempo;

    @Column(name = "corrida_ritmo")
    private Double ritmo; 

    @Column(name = "corrida_kcal")
    private Integer kcal;

    @Column(name = "corrida_distancia")
    private Double distancia;

    @ManyToOne
    @JoinColumn(name = "corrida_tipo_id")
    private Tipo tipo;

    @ManyToOne
    @JoinColumn(name = "corrida_rota_id")
    private Rota rota;

}
