package pt.iade.RunUp.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "caracteristica")
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caract_id")
    private Integer id;

    @Column(name = "caract_tipo")
    private String tipo;  

}
