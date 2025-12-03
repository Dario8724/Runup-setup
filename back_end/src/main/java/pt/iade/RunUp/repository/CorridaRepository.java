package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.entity.Corrida;

import java.time.LocalDateTime;
import java.util.List;

public interface CorridaRepository extends JpaRepository<Corrida, Integer> {

    List<Corrida> findByRotaId(Integer rotaId);

   // List<Corrida> findByUsuarioIdAndDataHoraInicioBetween(
     //   Integer usuarioId,
     //   LocalDateTime inicio,
     //   LocalDateTime fim
    //);
}
