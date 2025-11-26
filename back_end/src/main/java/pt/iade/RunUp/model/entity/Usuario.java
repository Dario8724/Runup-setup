package pt.iade.RunUp.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_nome")
    private String nome;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_senha")
    private String senha;

    @Column(name = "user_data_de_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "user_sexo")
    private String sexo;

    @Column(name = "user_peso")
    private Double peso;

    @Column(name = "user_altura")
    private Double altura;

    @Column(name = "user_experiencia")
    private String experiencia;

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

    // os outros getters/setters podes adicionar depois se precisares
}
