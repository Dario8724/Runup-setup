package pt.iade.RunUp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.iade.RunUp.model.dto.UserStatsDto;
import pt.iade.RunUp.model.entity.Corrida;
import pt.iade.RunUp.model.entity.MetaUsuario;
import pt.iade.RunUp.repository.MetaUsuarioRepository;
import pt.iade.RunUp.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final MetaUsuarioRepository metaUsuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          MetaUsuarioRepository metaUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.metaUsuarioRepository = metaUsuarioRepository;
    }

    @Transactional(readOnly = true)
    public UserStatsDto getUserStats(Integer userId) {

        // garante que o usuário existe
        usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<MetaUsuario> metas = metaUsuarioRepository
                .findByUsuario_IdOrderByCorrida_DataDesc(userId);

        long totalCorridas = 0L;
        double totalKm = 0.0;
        long totalSegundos = 0L;

        for (MetaUsuario mu : metas) {
            Corrida c = mu.getCorrida();
            if (c == null) continue;

            totalCorridas++;

            if (c.getDistancia() != null) {
                totalKm += c.getDistancia();
            }
            if (c.getTempo() != null) {
                totalSegundos += c.getTempo().toSecondOfDay();
            }
        }

        UserStatsDto dto = new UserStatsDto();
        dto.setTotalCorridas(totalCorridas);
        dto.setTotalKm(totalKm);
        dto.setTotalTempoSegundos(totalSegundos);

        return dto;
    }
}