package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.RunHistory;
import java.util.List;

public interface RunHistoryRepository extends JpaRepository<RunHistory, Long> {

    List<RunHistory> findByUsuarioId(Long usuarioId);

}
