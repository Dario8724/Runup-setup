package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.RunType;

public interface RunTypeRepository extends JpaRepository<RunType, Long> {
}
