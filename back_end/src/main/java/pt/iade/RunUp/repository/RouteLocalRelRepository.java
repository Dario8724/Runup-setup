package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.RouteLocalRel;

import java.util.List;

public interface RouteLocalRelRepository extends JpaRepository<RouteLocalRel, Integer> {
    List<RouteLocalRel> findByRotaId(Integer rotaId);
}
