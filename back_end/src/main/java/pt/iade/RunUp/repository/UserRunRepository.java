package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.UserRun;
import java.util.List;

public interface UserRunRepository extends JpaRepository<UserRun, Long> {

    List<UserRun> findByUserId(Long userId);
}
