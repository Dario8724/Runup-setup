package pt.iade.RunUp.model.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "rota")
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rota_id")
    private Integer id;

    @Column(name = "rota_nome")
    private String nome;

    @Column(name = "rota_elevacao")
    private Double elevacao;

    @Column(name = "rota_distancia_km")
    private Double distanciaKm;

    @Column(name = "rota_predefinida")
    private Boolean predefinida = false;

    @Column(name = "rota_cidade")
    private String cidade;

    @OneToMany(mappedBy = "rota", cascade = CascadeType.ALL)
    private List<LigacaoRotaLocal> locais;

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

    public Double getElevacao() {
        return elevacao;
    }

    public void setElevacao(Double elevacao) {
        this.elevacao = elevacao;
    }

    public Double getDistanciaKm() { 
        return distanciaKm; 
    }

    public void setDistanciaKm(Double distanciaKm) { 
        this.distanciaKm = distanciaKm; 
    }

    public Boolean getPredefinida() { 
        return predefinida; 
    }

    public void setPredefinida(Boolean predefinida) { 
        this.predefinida = predefinida; 
    }

    public String getCidade() { 
        return cidade; 
    }

    public void setCidade(String cidade) { 
        this.cidade = cidade; 
    }

    public List<LigacaoRotaLocal> getLocais() {
        return locais;
    }

    public void setLocais(List<LigacaoRotaLocal> locais) {
        this.locais = locais;
    }
}
