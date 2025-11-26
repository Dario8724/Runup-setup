package pt.iade.RunUp.model;

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

    @Column(name = "mu_meta_id")
    private Integer metaId;

}
