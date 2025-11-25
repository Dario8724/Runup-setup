package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.Run;

public interface RunRepository extends JpaRepository<Run, Long> {
}
