package pt.iade.RunUp.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "corrida")
public class Corrida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "corrida_id")
    private Integer id;

    @Column(name = "corrida_data")
    private LocalDate data;

    @Column(name = "corrida_tempo")
    private LocalTime tempo;

    @Column(name = "corrida_ritmo")
    private Double ritmo;

    @Column(name = "corrida_kcal")
    private Integer kcal;

    @Column(name = "corrida_distancia")
    private Double distancia;

    @ManyToOne
    @JoinColumn(name = "corrida_tipo_id")
    private Tipo tipo;

    @ManyToOne
    @JoinColumn(name = "corrida_rota_id")
    private Rota rota;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getTempo() {
        return tempo;
    }

    public void setTempo(LocalTime tempo) {
        this.tempo = tempo;
    }

    public Double getRitmo() {
        return ritmo;
    }

    public void setRitmo(Double ritmo) {
        this.ritmo = ritmo;
    }

    public Integer getKcal() {
        return kcal;
    }

    public void setKcal(Integer kcal) {
        this.kcal = kcal;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Rota getRota() {
        return rota;
    }

    public void setRota(Rota rota) {
        this.rota = rota;
    }
}
