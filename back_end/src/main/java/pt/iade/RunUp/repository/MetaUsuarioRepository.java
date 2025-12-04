package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.entity.MetaUsuario;

import java.util.List;
import java.util.Optional;

public interface MetaUsuarioRepository extends JpaRepository<MetaUsuario, Integer> {

    // todos os registros (meta ou corrida) de um usuário
    List<MetaUsuario> findByUsuario_Id(Integer usuarioId);

    // só registros que têm metaId preenchido (usado para metas)
    List<MetaUsuario> findByUsuario_IdAndMetaIdIsNotNull(Integer usuarioId);

    // registros ordenados pela data da corrida (usado para stats/records)
    List<MetaUsuario> findByUsuario_IdOrderByCorrida_DataDesc(Integer usuarioId);

    // usado no CorridaService.finalizarCorrida
    Optional<MetaUsuario> findFirstByCorrida_Id(Integer corridaId);

    // se ainda estiver a ser usado em algum lugar, pode manter:
    boolean existsByCorrida_Id(Integer corridaId);
}