package pt.iade.RunUp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.iade.RunUp.model.Usuario;
import pt.iade.RunUp.model.dto.UsuarioDTO;
import pt.iade.RunUp.repository.UsuarioRepository;

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
}
