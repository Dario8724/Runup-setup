package pt.iade.RunUp.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "local")
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "local_id")
    private Integer id;

    @Column(name = "local_nome")
    private String nome;

    @Column(name = "local_latitude")
    private Double latitude;

    @Column(name = "local_longitude")
    private Double longitude;

    @Column(name = "local_elevacao")
    private Double elevacao;

}
