package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);
}
