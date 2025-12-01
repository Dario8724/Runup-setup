package pt.iade.RunUp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.iade.RunUp.model.dto.CorridaResponse;
import pt.iade.RunUp.model.dto.CreateCorridaRequest;
import pt.iade.RunUp.model.dto.RoutePointDTO;
import pt.iade.RunUp.model.entity.*;
import pt.iade.RunUp.repository.*;
import pt.iade.RunUp.model.dto.FinalizarCorridaRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CorridaService {

    private final UsuarioRepository usuarioRepository;
    private final TipoRepository tipoRepository;
    private final RotaRepository rotaRepository;
    private final LocalRepository localRepository;
    private final LigacaoRotaLocalRepository ligacaoRotaLocalRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final CaracteristicaRotaRepository caracteristicaRotaRepository;
    private final CorridaRepository corridaRepository;
    private final MetaUsuarioRepository metaUsuarioRepository;

    public CorridaService(UsuarioRepository usuarioRepository,
            TipoRepository tipoRepository,
            RotaRepository rotaRepository,
            LocalRepository localRepository,
            LigacaoRotaLocalRepository ligacaoRotaLocalRepository,
            CaracteristicaRepository caracteristicaRepository,
            CaracteristicaRotaRepository caracteristicaRotaRepository,
            CorridaRepository corridaRepository,
            MetaUsuarioRepository metaUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tipoRepository = tipoRepository;
        this.rotaRepository = rotaRepository;
        this.localRepository = localRepository;
        this.ligacaoRotaLocalRepository = ligacaoRotaLocalRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.caracteristicaRotaRepository = caracteristicaRotaRepository;
        this.corridaRepository = corridaRepository;
        this.metaUsuarioRepository = metaUsuarioRepository;
    }

    @Transactional
    public CorridaResponse criarCorrida(CreateCorridaRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Tipo tipo = tipoRepository.findFirstByNome(request.getTipoNome())
                .orElseThrow(() -> new RuntimeException("Tipo não encontrado: " + request.getTipoNome()));

        Rota rota = new Rota();
        rota.setNome(request.getRouteName());
        rota.setElevacao(request.getElevacaoTotal());
        rota = rotaRepository.save(rota);

        List<RoutePointDTO> pontos = request.getPontos();
        if (pontos != null) {
            int ordem = 1;
            for (RoutePointDTO p : pontos) {

                Local local = new Local();
                local.setNome("Ponto " + ordem);
                local.setLatitude(p.getLatitude());
                local.setLongitude(p.getLongitude());
                local.setElevacao(p.getElevation());
                local = localRepository.save(local);

                LigacaoRotaLocal lr = new LigacaoRotaLocal();
                lr.setRota(rota);
                lr.setLocal(local);
                lr.setOrdem(ordem);
                ligacaoRotaLocalRepository.save(lr);

                ordem++;
            }
        }

        if (request.getFiltros() != null) {
            for (String filtro : request.getFiltros()) {
                Caracteristica caract = caracteristicaRepository.findByTipo(filtro)
                        .orElseGet(() -> {
                            Caracteristica nova = new Caracteristica();
                            nova.setTipo(filtro);
                            return caracteristicaRepository.save(nova);
                        });

                CaracteristicaRota cr = new CaracteristicaRota();
                cr.setRota(rota);
                cr.setCaracteristica(caract);
                caracteristicaRotaRepository.save(cr);
            }
        }

        Corrida corrida = new Corrida();
        corrida.setData(LocalDate.now());
        corrida.setDistancia(request.getDistanceKm());
        corrida.setTipo(tipo);
        corrida.setRota(rota);

        if (request.getTempoSegundos() != null) {
            LocalTime tempo = LocalTime.ofSecondOfDay(request.getTempoSegundos());
            corrida.setTempo(tempo);

            if (request.getDistanceKm() != null && request.getDistanceKm() > 0) {
                double pace = (request.getTempoSegundos() / 60.0) / request.getDistanceKm();
                corrida.setRitmo(Math.round(pace * 100.0) / 100.0);
            }
        }

        Integer kcal = request.getKcal();
        if (kcal == null && request.getDistanceKm() != null) {
            kcal = (int) Math.round(request.getDistanceKm() * 60);
        }
        corrida.setKcal(kcal);

        corrida = corridaRepository.save(corrida);

        MetaUsuario mu = new MetaUsuario();
        mu.setUsuario(usuario);
        mu.setCorrida(corrida);
        mu.setMetaId(null);
        metaUsuarioRepository.save(mu);

        CorridaResponse response = new CorridaResponse();
        response.setCorridaId(corrida.getId());
        response.setUserId(usuario.getId());
        response.setRouteName(rota.getNome());
        response.setTipoNome(tipo.getNome());
        response.setDistanceKm(corrida.getDistancia());
        response.setData(corrida.getData());
        response.setElevacaoTotal(rota.getElevacao());
        response.setKcal(corrida.getKcal());

        if (corrida.getTempo() != null) {
            long segundos = corrida.getTempo().toSecondOfDay();
            response.setTempoSegundos(segundos);
            if (corrida.getRitmo() != null) {
                response.setPaceMinPorKm(corrida.getRitmo());
            }
        }

        response.setPontos(request.getPontos());
        response.setFiltros(request.getFiltros());

        return response;
    }

    @Transactional
    public void finalizarCorrida(Integer corridaId, FinalizarCorridaRequest req) {
        Corrida corrida = corridaRepository.findById(corridaId)
                .orElseThrow(() -> new RuntimeException("Corrida não encontrada: " + corridaId));

        if (!metaUsuarioRepository.existsByCorrida_Id(corridaId)) {

            Integer userId = req.getUserId(); 

            Usuario usuario = usuarioRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            MetaUsuario mu = new MetaUsuario();
            mu.setUsuario(usuario);
            mu.setCorrida(corrida);
            mu.setMetaId(null);

            metaUsuarioRepository.save(mu);
        }

        Double distKm = req.getDistanciaRealKm();
        Long durSeg = req.getDuracaoSegundos();

        corrida.setDistancia(distKm);
        corrida.setTempo(LocalTime.ofSecondOfDay(durSeg));
        corrida.setKcal(req.getKcal());

        if (distKm != null && distKm > 0 && durSeg != null) {
            double paceMinPorKm = (durSeg / 60.0) / distKm;
            corrida.setRitmo(paceMinPorKm);
        }

        corridaRepository.save(corrida);
    }
}