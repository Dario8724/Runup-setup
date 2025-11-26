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

    @OneToMany(mappedBy = "rota", cascade = CascadeType.ALL)
    private List<LigacaoRotaLocal> locais;

    // GETTERS E SETTERS

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

    public List<LigacaoRotaLocal> getLocais() {
        return locais;
    }

    public void setLocais(List<LigacaoRotaLocal> locais) {
        this.locais = locais;
    }
}
