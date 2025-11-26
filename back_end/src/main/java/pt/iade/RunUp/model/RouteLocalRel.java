package pt.iade.RunUp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lr")
@Data
public class RouteLocalRel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lr_id")
    private Integer id;

    @Column(name = "lr_rota_id")
    private Integer rotaId;

    @Column(name = "lr_local_id")
    private Integer localId;

    @Column(name = "lr_ordem")
    private Integer ordem;
}
