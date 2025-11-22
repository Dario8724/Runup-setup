package pt.iade.RunUp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.iade.RunUp.model.Usuario;
import pt.iade.RunUp.model.dto.UsuarioDTO;
import pt.iade.RunUp.repository.UsuarioRepository;
import pt.iade.RunUp.model.dto.LoginResponseDTO;
import pt.iade.RunUp.model.dto.LoginRequestDTO;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario criarUsuario(UsuarioDTO dto) {

        if (usuarioRepository.existsByEmail(dto.email)) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario u = new Usuario();
        u.setNome(dto.nome);
        u.setEmail(dto.email);
        u.setSenha(dto.senha);
        u.setData_aniversario(dto.data_aniversario);
        u.setSexo(dto.sexo);
        u.setPeso(dto.peso);
        u.setAltura(dto.altura);
        u.setExperiencia(dto.experiencia);

        return usuarioRepository.save(u);
    }

    public LoginResponseDTO login(String email, String senha) {
        Usuario u = usuarioRepository.findByEmail(email);

        if (u == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        if (!u.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta");
        }

        return new LoginResponseDTO(
            u.getId_usuario(),
            u.getNome(),
            u.getEmail()
        );
    }
}
