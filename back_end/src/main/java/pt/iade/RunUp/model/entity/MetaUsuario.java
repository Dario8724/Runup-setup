package pt.iade.RunUp.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mu")
public class MetaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mu_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "mu_user_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "mu_corrida_id")
    private Corrida corrida;

    // se quiseres ligar com a tabela "meta" depois, dá pra mudar
    @Column(name = "mu_meta_id")
    private Integer metaId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Corrida getCorrida() {
        return corrida;
    }

    public void setCorrida(Corrida corrida) {
        this.corrida = corrida;
    }

    public Integer getMetaId() {
        return metaId;
    }

    public void setMetaId(Integer metaId) {
        this.metaId = metaId;
    }
}
