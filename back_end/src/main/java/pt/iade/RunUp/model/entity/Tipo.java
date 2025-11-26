package pt.iade.RunUp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo")
public class Tipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_id")
    private Integer id;

    @Column(name = "tipo_nome")
    private String nome; 

}
