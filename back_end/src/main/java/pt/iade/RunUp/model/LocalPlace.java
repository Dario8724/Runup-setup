package pt.iade.RunUp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "local")
@Data
public class LocalPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "local_id")
    private Integer id;

    @Column(name = "local_nome")
    private String nome;
}
