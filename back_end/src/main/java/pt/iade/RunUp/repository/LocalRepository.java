package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.LocalPlace;

import java.util.Optional;

public interface LocalRepository extends JpaRepository<LocalPlace, Integer> {
    Optional<LocalPlace> findByNome(String nome);
}
