package pt.iade.RunUp.model.entity;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Rota getRota() {
        return rota;
    }

    public void setRota(Rota rota) {
        this.rota = rota;
    }

    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(Caracteristica caracteristica) {
        this.caracteristica = caracteristica;
    }
}
