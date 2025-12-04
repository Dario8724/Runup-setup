package pt.iade.RunUp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.iade.RunUp.model.dto.GoalDto;
import pt.iade.RunUp.model.dto.UpdateGoalsRequest;
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

    public void createGoalsForUser(Usuario usuario) {

        String experiencia = Optional.ofNullable(usuario.getExperiencia())
                .orElse("Iniciante")
                .toLowerCase();

        double kmPorTreino;
        int treinosPorSemana;

        if (experiencia.contains("iniciante")) {
            kmPorTreino = 3.0;
            treinosPorSemana = 3;
        } else if (experiencia.contains("intermedi")) { 
            kmPorTreino = 6.0;
            treinosPorSemana = 4;
        } else { 
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

    public List<GoalDto> getGoalsForUser(Integer userId) {

        if (!usuarioRepository.existsById(userId)) {
            throw new RuntimeException("Usuário não encontrado");
        }

        List<MetaUsuario> metasUsuario = metaUsuarioRepository.findByUsuario_IdAndMetaIdIsNotNull(userId);

        List<MetaUsuario> muComCorrida =
                metaUsuarioRepository.findByUsuario_IdOrderByCorrida_DataDesc(userId);

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

    @Transactional
    public void updateGoalsForUser(Integer userId, UpdateGoalsRequest request) {

        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // todos os links de META (não de corrida) desse usuário
        List<MetaUsuario> links = metaUsuarioRepository
                .findByUsuario_IdAndMetaIdIsNotNull(userId);

        Meta metaSemanal = null;
        Meta metaMensal = null;

        // descobre quais metas são semanal / mensal
        for (MetaUsuario mu : links) {
            Integer metaId = mu.getMetaId();
            if (metaId == null) continue;

            Meta meta = metaRepository.findById(metaId).orElse(null);
            if (meta == null) continue;

            String nome = meta.getNome() != null ? meta.getNome() : "";

            if (nome.equalsIgnoreCase("Meta Semanal")) {
                metaSemanal = meta;
            } else if (nome.equalsIgnoreCase("Meta Mensal")) {
                metaMensal = meta;
            }
        }

        // ----- atualizar / criar Meta Semanal -----
        if (request.getMetaSemanalKm() != null) {

            if (metaSemanal == null) {
                Meta nova = new Meta();
                nova.setNome("Meta Semanal");
                nova.setDistancia(request.getMetaSemanalKm());
                nova = metaRepository.save(nova);

                MetaUsuario mu = new MetaUsuario();
                mu.setUsuario(usuario);
                mu.setMetaId(nova.getId());
                mu.setCorrida(null); // link só com meta
                metaUsuarioRepository.save(mu);

            } else {
                metaSemanal.setDistancia(request.getMetaSemanalKm());
                metaRepository.save(metaSemanal);
            }
        }

        // ----- atualizar / criar Meta Mensal -----
        if (request.getMetaMensalKm() != null) {

            if (metaMensal == null) {
                Meta nova = new Meta();
                nova.setNome("Meta Mensal");
                nova.setDistancia(request.getMetaMensalKm());
                nova = metaRepository.save(nova);

                MetaUsuario mu = new MetaUsuario();
                mu.setUsuario(usuario);
                mu.setMetaId(nova.getId());
                mu.setCorrida(null);
                metaUsuarioRepository.save(mu);

            } else {
                metaMensal.setDistancia(request.getMetaMensalKm());
                metaRepository.save(metaMensal);
            }
        }
    }
}
