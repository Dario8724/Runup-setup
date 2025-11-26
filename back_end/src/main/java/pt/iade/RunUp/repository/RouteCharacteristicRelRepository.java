package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.RouteCharacteristicRel;

import java.util.List;

public interface RouteCharacteristicRelRepository extends JpaRepository<RouteCharacteristicRel, Integer> {
    List<RouteCharacteristicRel> findByRotaId(Integer rotaId);
}
