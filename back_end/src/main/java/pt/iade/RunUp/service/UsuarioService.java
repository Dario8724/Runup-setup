package pt.iade.RunUp.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.iade.RunUp.model.dto.PersonalRecordDto;
import pt.iade.RunUp.model.dto.UserStatsDto;
import pt.iade.RunUp.model.dto.WeeklyStatsDto;
import pt.iade.RunUp.model.entity.Corrida;
import pt.iade.RunUp.model.entity.MetaUsuario;
import pt.iade.RunUp.repository.MetaUsuarioRepository;
import pt.iade.RunUp.repository.UsuarioRepository;

@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final MetaUsuarioRepository metaUsuarioRepository;

  public UsuarioService(
    UsuarioRepository usuarioRepository,
    MetaUsuarioRepository metaUsuarioRepository
  ) {
    this.usuarioRepository = usuarioRepository;
    this.metaUsuarioRepository = metaUsuarioRepository;
  }

  @Transactional(readOnly = true)
  public UserStatsDto getUserStats(Integer userId) {
    usuarioRepository
      .findById(userId)
      .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    List<MetaUsuario> metas =
      metaUsuarioRepository.findByUsuario_IdOrderByCorrida_DataDesc(userId);

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

  @Transactional(readOnly = true)
  public WeeklyStatsDto getWeeklyStats(Integer userId) {
    usuarioRepository
      .findById(userId)
      .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    LocalDate limite = LocalDate.now().minusDays(7);

    var metas = metaUsuarioRepository.findByUsuario_IdOrderByCorrida_DataDesc(
      userId
    );

    double totalKm = 0.0;
    int totalKcal = 0;
    long totalSegundos = 0L;

    for (MetaUsuario mu : metas) {
      Corrida c = mu.getCorrida();
      if (c == null) continue;

      LocalDate data = c.getData();
      if (data == null) continue;

      if (data.isBefore(limite)) break;

      if (c.getDistancia() != null) {
        totalKm += c.getDistancia();
      }
      if (c.getKcal() != null) {
        totalKcal += c.getKcal();
      }
      if (c.getTempo() != null) {
        totalSegundos += c.getTempo().toSecondOfDay();
      }
    }

    WeeklyStatsDto dto = new WeeklyStatsDto();
    dto.setDistanciaTotalKm(totalKm);
    dto.setCaloriasTotais(totalKcal);
    dto.setTempoTotalSegundos(totalSegundos);
    return dto;
  }

  @Transactional(readOnly = true)
  public PersonalRecordDto getMaiorDistancia(Integer userId) {
    usuarioRepository
      .findById(userId)
      .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    var metas = metaUsuarioRepository.findByUsuario_IdOrderByCorrida_DataDesc(
      userId
    );

    Corrida melhor = null;

    for (MetaUsuario mu : metas) {
      Corrida c = mu.getCorrida();
      if (c == null || c.getDistancia() == null) continue;

      if (melhor == null || c.getDistancia() > melhor.getDistancia()) {
        melhor = c;
      }
    }

    PersonalRecordDto dto = new PersonalRecordDto();
    if (melhor != null) {
      dto.setMaiorDistanciaKm(melhor.getDistancia());
      dto.setDataCorrida(melhor.getData());
      dto.setCorridaId(melhor.getId());
    } else {
      dto.setMaiorDistanciaKm(0.0);
      dto.setDataCorrida(null);
      dto.setCorridaId(null);
    }

    return dto;
  }
}