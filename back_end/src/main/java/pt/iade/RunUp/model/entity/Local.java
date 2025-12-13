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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getElevacao() {
        return elevacao;
    }

    public void setElevacao(Double elevacao) {
        this.elevacao = elevacao;
    }
}
