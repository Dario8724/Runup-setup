package pt.iade.RunUp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rota")
@Data
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rota_id")
    private Integer id;

    @Column(name = "rota_nome")
    private String name;

    @Column(name = "rota_elevacao")
    private Double elevation;
}