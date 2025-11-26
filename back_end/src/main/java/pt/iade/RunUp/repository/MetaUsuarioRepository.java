package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.MetaUsuario;

import java.util.List;

public interface MetaUsuarioRepository extends JpaRepository<MetaUsuario, Integer> {

    List<MetaUsuario> findByUsuarioId(Integer userId);
}
