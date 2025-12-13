package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.entity.Rota;

import java.util.List;

public interface RotaRepository extends JpaRepository<Rota, Integer> {

    List<Rota> findByPredefinidaTrueAndCidadeIgnoreCase(String cidade);
}
