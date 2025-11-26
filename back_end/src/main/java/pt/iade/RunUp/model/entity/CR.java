package pt.iade.RunUp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cr")
public class CaracteristicaRota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cr_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cr_rota_id")
    private Rota rota;

    @ManyToOne
    @JoinColumn(name = "cr_caract_id")
    private Caracteristica caracteristica;

}
