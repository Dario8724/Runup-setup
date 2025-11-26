package pt.iade.RunUp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_nome", nullable = false)
    private String nome;

    @Column(name = "user_email", nullable = false)
    private String email;

    @Column(name = "user_senha", nullable = false)
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

}
