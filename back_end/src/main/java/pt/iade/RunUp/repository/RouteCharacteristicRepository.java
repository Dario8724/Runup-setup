package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.RouteCharacteristic;

import java.util.Optional;

public interface RouteCharacteristicRepository extends JpaRepository<RouteCharacteristic, Integer> {
    Optional<RouteCharacteristic> findByTipo(String tipo);
}
