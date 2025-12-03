package pt.iade.RunUp.auth;

public class LoginResponse {
    public Integer userId;
    public String nome;
    public String email;

    public LoginResponse(Integer userId, String nome, String email) {
        this.userId = userId;
        this.nome = nome;
        this.email = email;
    }
}
