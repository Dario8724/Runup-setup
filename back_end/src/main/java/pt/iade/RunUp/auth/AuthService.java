package pt.iade.RunUp.auth;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pt.iade.RunUp.model.entity.Usuario;
import pt.iade.RunUp.repository.UsuarioRepository;
import pt.iade.RunUp.service.GoalService;

import java.time.LocalDate;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final GoalService goalService;

    public AuthService(UsuarioRepository usuarioRepository, GoalService goalService) {
        this.usuarioRepository = usuarioRepository;
        this.goalService = goalService;
    }

    public LoginResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email)) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario u = new Usuario();
        u.setNome(request.nome);
        u.setEmail(request.email);
        u.setSenha(passwordEncoder.encode(request.senha));

        if (request.dataDeNascimento != null && !request.dataDeNascimento.isBlank()) {
            u.setDataNascimento(LocalDate.parse(request.dataDeNascimento));
        }

        u.setSexo(request.sexo);
        u.setPeso(request.peso);
        u.setAltura(request.altura);
        u.setExperiencia(request.experiencia);

        Usuario saved = usuarioRepository.save(u);

        goalService.createGoalsForUser(saved);

        return new LoginResponse(saved.getId(), saved.getNome(), saved.getEmail());
    }

    public LoginResponse login(LoginRequest request) {
        Usuario u = usuarioRepository.findByEmail(request.email)
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(request.senha, u.getSenha())) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        return new LoginResponse(u.getId(), u.getNome(), u.getEmail());
    }
}
