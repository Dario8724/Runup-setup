package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
