package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.Caracteristica;

import java.util.Optional;

public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Integer> {

    Optional<Caracteristica> findByTipo(String tipo);
}
