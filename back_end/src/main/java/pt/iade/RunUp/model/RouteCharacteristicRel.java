package pt.iade.RunUp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cr")
@Data
public class RouteCharacteristicRel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cr_id")
    private Integer id;

    @Column(name = "cr_rota_id")
    private Integer rotaId;

    @Column(name = "cr_caract_id")
    private Integer caractId;
}
