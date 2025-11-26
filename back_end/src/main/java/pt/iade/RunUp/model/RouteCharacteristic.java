package pt.iade.RunUp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "caracteristica")
@Data
public class RouteCharacteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caract_id")
    private Integer id;

    @Column(name = "caract_tipo")
    private String tipo;
}
