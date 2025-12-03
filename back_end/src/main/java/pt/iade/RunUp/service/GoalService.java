package pt.iade.RunUp.service;

import org.springframework.stereotype.Service;
import pt.iade.RunUp.model.dto.GoalDto;
import pt.iade.RunUp.model.entity.Corrida;
import pt.iade.RunUp.model.entity.Meta;
import pt.iade.RunUp.model.entity.MetaUsuario;
import pt.iade.RunUp.model.entity.Usuario;
import pt.iade.RunUp.repository.MetaRepository;
import pt.iade.RunUp.repository.MetaUsuarioRepository;
import pt.iade.RunUp.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoalService {

    private final MetaRepository metaRepository;
    private final MetaUsuarioRepository metaUsuarioRepository;
    private final UsuarioRepository usuarioRepository;

    public GoalService(MetaRepository metaRepository,
                       MetaUsuarioRepository metaUsuarioRepository,
                       UsuarioRepository usuarioRepository) {
        this.metaRepository = metaRepository;
        this.metaUsuarioRepository = metaUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // 🔹 chamado no register() – cria meta semanal e mensal
    public void createGoalsForUser(Usuario usuario) {

        String experiencia = Optional.ofNullable(usuario.getExperiencia())
                .orElse("Iniciante")
                .toLowerCase();

        double kmPorTreino;
        int treinosPorSemana;

        if (experiencia.contains("iniciante")) {
            kmPorTreino = 3.0;
            treinosPorSemana = 3;
        } else if (experiencia.contains("intermedi")) { // "Intermediário"
            kmPorTreino = 6.0;
            treinosPorSemana = 4;
        } else { // avançado e outros
            kmPorTreino = 10.0;
            treinosPorSemana = 5;
        }

        double metaSemanal = kmPorTreino * treinosPorSemana;
        double metaMensal = metaSemanal * 4;

        // META SEMANAL
        Meta metaSemana = new Meta();
        metaSemana.setNome("Meta Semanal");
        metaSemana.setDistancia(metaSemanal);
        metaSemana = metaRepository.save(metaSemana);

        MetaUsuario muSemana = new MetaUsuario();
        muSemana.setMetaId(metaSemana.getId());
        muSemana.setUsuario(usuario);
        muSemana.setCorrida(null);
        metaUsuarioRepository.save(muSemana);

        // META MENSAL
        Meta metaMes = new Meta();
        metaMes.setNome("Meta Mensal");
        metaMes.setDistancia(metaMensal);
        metaMes = metaRepository.save(metaMes);

        MetaUsuario muMes = new MetaUsuario();
        muMes.setMetaId(metaMes.getId());
        muMes.setUsuario(usuario);
        muMes.setCorrida(null);
        metaUsuarioRepository.save(muMes);
    }

    // 🔹 usado pelo GoalController – devolve metas + progresso
    public List<GoalDto> getGoalsForUser(Integer userId) {

        if (!usuarioRepository.existsById(userId)) {
            throw new RuntimeException("Usuário não encontrado");
        }

        // metas associadas ao usuário (semanal + mensal)
        List<MetaUsuario> metasUsuario = metaUsuarioRepository.findByUsuario_Id(userId);

        // corridas associadas ao usuário via mu (as que já existem hoje)
        List<MetaUsuario> muComCorrida =
                metaUsuarioRepository.findByUsuario_IdOrderByCorrida_DataDesc(userId);

        // extrair corridas únicas
        Map<Integer, Corrida> corridasPorId = new HashMap<>();
        for (MetaUsuario mu : muComCorrida) {
            Corrida corrida = mu.getCorrida();
            if (corrida != null && corrida.getId() != null) {
                corridasPorId.putIfAbsent(corrida.getId(), corrida);
            }
        }

        List<Corrida> corridas = new ArrayList<>(corridasPorId.values());

        LocalDate hoje = LocalDate.now();
        LocalDate semanaAtras = hoje.minusDays(7);
        LocalDate primeiroDiaMes = hoje.withDayOfMonth(1);

        // calcular progresso geral
        double progressoSemanal = corridas.stream()
                .filter(c -> {
                    LocalDate data = c.getData();
                    return data != null && ( !data.isBefore(semanaAtras) && !data.isAfter(hoje) );
                })
                .mapToDouble(c -> Optional.ofNullable(c.getDistancia()).orElse(0.0))
                .sum();

        double progressoMensal = corridas.stream()
                .filter(c -> {
                    LocalDate data = c.getData();
                    return data != null && ( !data.isBefore(primeiroDiaMes) && !data.isAfter(hoje) );
                })
                .mapToDouble(c -> Optional.ofNullable(c.getDistancia()).orElse(0.0))
                .sum();

        List<GoalDto> goals = new ArrayList<>();

        for (MetaUsuario mu : metasUsuario) {

            Meta meta = metaRepository.findById(mu.getMetaId()).orElse(null);
            if (meta == null) continue;

            double progresso = 0.0;

            if ("Meta Semanal".equalsIgnoreCase(meta.getNome())) {
                progresso = progressoSemanal;
            } else if ("Meta Mensal".equalsIgnoreCase(meta.getNome())) {
                progresso = progressoMensal;
            }

            goals.add(new GoalDto(
                    meta.getNome(),
                    Optional.ofNullable(meta.getDistancia()).orElse(0.0),
                    progresso
            ));
        }

        return goals;
    }
}
