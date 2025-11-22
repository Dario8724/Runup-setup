package pt.iade.RunUp.model.dto;

public class LoginResponseDTO {
    public Long id;
    public String nome;
    public String email;

    public LoginResponseDTO() {}

    public LoginResponseDTO(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }
}
