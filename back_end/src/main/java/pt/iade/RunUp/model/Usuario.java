package pt.iade.RunUp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Usuario", schema = "RunUp")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    private String nome;
    private String email;
    private String senha;
    private LocalDate data_aniversario;
    private String sexo;
    private Double peso;
    private Double altura;
    private String experiencia; // NOVO CAMPO

    public Usuario() {}

    // Getters e Setters
    public Long getId_usuario() { return id_usuario; }
    public void setId_usuario(Long id_usuario) { this.id_usuario = id_usuario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public LocalDate getData_aniversario() { return data_aniversario; }
    public void setData_aniversario(LocalDate data_aniversario) { this.data_aniversario = data_aniversario; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }
}
