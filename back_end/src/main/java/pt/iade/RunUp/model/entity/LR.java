package pt.iade.RunUp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lr")
public class LigacaoRotaLocal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lr_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lr_rota_id")
    private Rota rota;

    @ManyToOne
    @JoinColumn(name = "lr_local_id")
    private Local local;

    @Column(name = "lr_ordem")
    private Integer ordem;

    // getters e setters
}
