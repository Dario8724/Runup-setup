package pt.iade.RunUp.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "caracteristica")
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caract_id")
    private Integer id;

    @Column(name = "caract_tipo")
    private String tipo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
