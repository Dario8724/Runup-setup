package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.CaracteristicaRota;

import java.util.List;

public interface CaracteristicaRotaRepository extends JpaRepository<CaracteristicaRota, Integer> {

    List<CaracteristicaRota> findByRotaId(Integer rotaId);
}
