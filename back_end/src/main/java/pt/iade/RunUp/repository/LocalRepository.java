package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.entity.Local;

public interface LocalRepository extends JpaRepository<Local, Integer> {
}
