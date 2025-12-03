package pt.iade.RunUp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.iade.RunUp.model.dto.UpdateUserProfileRequest;
import pt.iade.RunUp.model.dto.UserProfileDto;
import pt.iade.RunUp.model.entity.Usuario;
import pt.iade.RunUp.repository.UsuarioRepository;
import pt.iade.RunUp.repository.MetaUsuarioRepository;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UserProfileService {

    private final UsuarioRepository usuarioRepository;
    private final MetaUsuarioRepository metaUsuarioRepository;

    public UserProfileService(UsuarioRepository usuarioRepository, MetaUsuarioRepository metaUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.metaUsuarioRepository = metaUsuarioRepository;
    }

    // --------- GET PROFILE ----------
    @Transactional(readOnly = true)
    public UserProfileDto getProfile(Integer id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return toDto(u);
    }

    // --------- UPDATE PROFILE ----------
    @Transactional
    public UserProfileDto updateProfile(Integer id, UpdateUserProfileRequest req) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (req.getEmail() != null &&
                usuarioRepository.existsByEmailAndIdNot(req.getEmail(), id)) {
            throw new RuntimeException("Email já está a ser utilizado por outro usuário");
        }

        if (req.getNome() != null) {
            u.setNome(req.getNome());
        }

        if (req.getEmail() != null) {
            u.setEmail(req.getEmail());
        }

        if (req.getDataNascimento() != null && !req.getDataNascimento().isBlank()) {
            LocalDate data = LocalDate.parse(req.getDataNascimento());
            u.setDataNascimento(data);
        }

        if (req.getPeso() != null) {
            u.setPeso(req.getPeso());
        }

        if (req.getAltura() != null) {
            u.setAltura(req.getAltura());
        }

        if (req.getSexo() != null) {
            u.setSexo(req.getSexo());
        }

        Usuario saved = usuarioRepository.save(u);
        return toDto(saved);
    }

    // --------- DELETE USER ----------
    @Transactional
    public void deleteUser(Integer id) {
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 1) apagar ligações meta-usuario para este user
        var links = metaUsuarioRepository.findByUsuario_Id(id);
        metaUsuarioRepository.deleteAll(links);

        // 2) apagar o usuário em si
        usuarioRepository.delete(user);
    }

    // --------- mapper helper ----------
    private UserProfileDto toDto(Usuario u) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(u.getId());
        dto.setNome(u.getNome());
        dto.setEmail(u.getEmail());

        if (u.getDataNascimento() != null) {
            dto.setDataNascimento(u.getDataNascimento().toString());
            int idade = Period.between(u.getDataNascimento(), LocalDate.now()).getYears();
            dto.setIdade(idade);
        }

        dto.setPeso(u.getPeso());
        dto.setAltura(u.getAltura());
        dto.setSexo(u.getSexo());

        return dto;
    }
}