package pt.iade.RunUp.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "meta")
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meta_id")
    private Integer id;

    @Column(name = "meta_nome", length = 100)
    private String nome;

    @Column(name = "meta_distancia", precision = 10, scale = 2)
    private Double distancia; 

    // Getters e setters
    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public Double getDistancia() { return distancia; }

    public void setDistancia(Double distancia) { this.distancia = distancia; }
}
