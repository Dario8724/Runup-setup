package pt.iade.RunUp.model;

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

}
