package pt.iade.RunUp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.iade.RunUp.model.dto.*;
import pt.iade.RunUp.integration.GoogleDirectionsClient;
import pt.iade.RunUp.integration.GoogleElevationClient;
import pt.iade.RunUp.integration.GooglePlacesClient;
import pt.iade.RunUp.integration.WeatherClient;
import pt.iade.RunUp.model.entity.*;
import pt.iade.RunUp.repository.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CorridaGenerationService {

    private final UsuarioRepository usuarioRepository;
    private final TipoRepository tipoRepository;
    private final RotaRepository rotaRepository;
    private final LocalRepository localRepository;
    private final LigacaoRotaLocalRepository ligacaoRotaLocalRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final CaracteristicaRotaRepository caracteristicaRotaRepository;
    private final CorridaRepository corridaRepository;
    private final MetaUsuarioRepository metaUsuarioRepository;

    private final GoogleDirectionsClient directionsClient;
    private final GoogleElevationClient elevationClient;
    private final GooglePlacesClient placesClient;
    private final WeatherClient weatherClient;

    public CorridaGenerationService(UsuarioRepository usuarioRepository,
                                    TipoRepository tipoRepository,
                                    RotaRepository rotaRepository,
                                    LocalRepository localRepository,
                                    LigacaoRotaLocalRepository ligacaoRotaLocalRepository,
                                    CaracteristicaRepository caracteristicaRepository,
                                    CaracteristicaRotaRepository caracteristicaRotaRepository,
                                    CorridaRepository corridaRepository,
                                    MetaUsuarioRepository metaUsuarioRepository,
                                    GoogleDirectionsClient directionsClient,
                                    GoogleElevationClient elevationClient,
                                    GooglePlacesClient placesClient,
                                    WeatherClient weatherClient) {
        this.usuarioRepository = usuarioRepository;
        this.tipoRepository = tipoRepository;
        this.rotaRepository = rotaRepository;
        this.localRepository = localRepository;
        this.ligacaoRotaLocalRepository = ligacaoRotaLocalRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.caracteristicaRotaRepository = caracteristicaRotaRepository;
        this.corridaRepository = corridaRepository;
        this.metaUsuarioRepository = metaUsuarioRepository;
        this.directionsClient = directionsClient;
        this.elevationClient = elevationClient;
        this.placesClient = placesClient;
        this.weatherClient = weatherClient;
    }

    @Transactional
    public CorridaGeradaResponse gerarCorrida(GenerateCorridaRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String nomeTipo = request.getTipoAtividade() == TipoAtividade.CORRIDA ? "CORRIDA" : "CAMINHADA";

        Tipo tipo = tipoRepository.findFirstByNome(nomeTipo)
                .orElseThrow(() -> new RuntimeException("Tipo não encontrado: " + nomeTipo));

        if (request.getFiltros() != null &&
                request.getFiltros().contains(FiltroRota.ENSOLARADA)) {
        }

        double targetLat = request.getStartLatitude();
        double targetLng = request.getStartLongitude();

        if (request.getFiltros() != null && !request.getFiltros().isEmpty()) {
            if (request.getFiltros().contains(FiltroRota.PERTO_PARQUE)) {

            } else if (request.getFiltros().contains(FiltroRota.PERTO_PRAIA)) {

            }
        }

        double distanceKm = request.getDistanceKm() != null ? request.getDistanceKm() : 5.0;

        List<RoutePointDTO> pontos = directionsClient.gerarPontosDaRota(
            request.getStartLatitude(),
            request.getStartLongitude(),
            distanceKm,
            request.getTipoAtividade()
        );

        double totalElevationGain = 0.0;

        double paceMinPerKm = request.getTipoAtividade() == TipoAtividade.CORRIDA ? 6.0 : 12.0;
        double durationMinutes = distanceKm * paceMinPerKm;
        long durationSeconds = Math.round(durationMinutes * 60);

        int estimatedCalories = request.getTipoAtividade() == TipoAtividade.CORRIDA
                ? (int) Math.round(distanceKm * 65)
                : (int) Math.round(distanceKm * 40);

        Rota rota = new Rota();
        rota.setNome(request.getRouteName());
        rota.setElevacao(totalElevationGain);
        rota = rotaRepository.save(rota);

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

        if (request.getFiltros() != null) {
            for (FiltroRota filtro : request.getFiltros()) {
                if (filtro == FiltroRota.SEM_FILTRO) continue;

                String nomeCaract = filtro.name();
                Caracteristica caract = caracteristicaRepository.findByTipo(nomeCaract)
                        .orElseGet(() -> {
                            Caracteristica nova = new Caracteristica();
                            nova.setTipo(nomeCaract);
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
        corrida.setDistancia(distanceKm);
        corrida.setTipo(tipo);
        corrida.setRota(rota);

        LocalTime tempoEstimado = LocalTime.ofSecondOfDay(durationSeconds);
        corrida.setTempo(tempoEstimado);
        corrida.setRitmo(Math.round(paceMinPerKm * 100.0) / 100.0);
        corrida.setKcal(estimatedCalories);

        corrida = corridaRepository.save(corrida);

        MetaUsuario mu = new MetaUsuario();
        mu.setUsuario(usuario);
        mu.setCorrida(corrida);
        mu.setMetaId(null); 
        metaUsuarioRepository.save(mu);

        CorridaGeradaResponse resp = new CorridaGeradaResponse();
        resp.setCorridaId(corrida.getId());
        resp.setUserId(usuario.getId());
        resp.setRouteName(rota.getNome());
        resp.setTipoAtividade(request.getTipoAtividade());
        resp.setDistanceKm(distanceKm);
        resp.setEstimatedDurationSeconds(durationSeconds);
        resp.setPaceMinPerKm(paceMinPerKm);
        resp.setEstimatedCalories(estimatedCalories);
        resp.setTotalElevationGain(totalElevationGain);
        resp.setDataCriacao(corrida.getData());
        resp.setFiltrosAplicados(request.getFiltros());
        resp.setPontos(pontos);

        return resp;
    }
}
