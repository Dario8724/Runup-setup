package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.entity.LigacaoRotaLocal;

import java.util.List;

public interface LigacaoRotaLocalRepository extends JpaRepository<LigacaoRotaLocal, Integer> {

    List<LigacaoRotaLocal> findByRotaIdOrderByOrdemAsc(Integer rotaId);
}
