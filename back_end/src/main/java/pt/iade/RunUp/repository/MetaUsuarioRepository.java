package pt.iade.RunUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.RunUp.model.entity.MetaUsuario;

import java.util.List;

public interface MetaUsuarioRepository extends JpaRepository<MetaUsuario, Integer> {

    List<MetaUsuario> findByUsuario_IdOrderByCorrida_DataDesc(Integer usuarioId);

    List<MetaUsuario> findByUsuario_Id(Integer usuarioId);

    boolean existsByCorrida_Id(Integer corridaId);
}
