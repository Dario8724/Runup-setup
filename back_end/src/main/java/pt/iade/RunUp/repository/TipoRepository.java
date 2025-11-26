package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.Tipo;

import java.util.Optional;

public interface TipoRepository extends JpaRepository<Tipo, Integer> {

    Optional<Tipo> findByNome(String nome);
}
