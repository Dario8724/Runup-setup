package pt.iade.RunUp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Rota")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "distancia_km")
    private Double distanciaKm;

    @Column(name = "elevacao")
    private Double elevacao;

    @Column(name = "nome")
    private String nome;

    @Column(name = "ponto_final")
    private String pontoFinal;

    @Column(name = "ponto_inicial")
    private String pontoInicial;

    @Column(name = "tipo")
    private String tipo; 

    @Column(name = "filtro")
    private String filtro; 
}
