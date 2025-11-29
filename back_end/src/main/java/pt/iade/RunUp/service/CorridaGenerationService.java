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
import java.util.Map;

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

        // 1) Buscar usuário
        Usuario usuario = usuarioRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2) Definir tipo (CORRIDA ou CAMINHADA)
        String nomeTipo = request.getTipoAtividade() == TipoAtividade.CORRIDA ? "CORRIDA" : "CAMINHADA";

        Tipo tipo = tipoRepository.findFirstByNome(nomeTipo)
                .orElseThrow(() -> new RuntimeException("Tipo não encontrado: " + nomeTipo));

        // 3) Coordenadas iniciais (onde o user está)
        double startLat = request.getStartLatitude();
        double startLng = request.getStartLongitude();

        // 4) Ponto de partida efetivo da rota (pode ser ajustado pelos filtros)
        double routeStartLat = startLat;
        double routeStartLng = startLng;

        String weatherCondition = null;
        boolean ensolaradaAtendida = true; // por default assumimos que sim

        // 5) Filtros que mexem no ponto de partida (PERTO_PARQUE / PERTO_PRAIA)
        if (request.getFiltros() != null && !request.getFiltros().isEmpty()) {

            // PERTO_PARQUE → começar num parque próximo
            if (request.getFiltros().contains(FiltroRota.PERTO_PARQUE)) {
                try {
                    Map<String, Object> placesResp = placesClient.searchNearby(startLat, startLng, 1500, "park");

                    if (placesResp != null && placesResp.containsKey("results")) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> results = (List<Map<String, Object>>) placesResp.get("results");

                        if (results != null && !results.isEmpty()) {
                            Map<String, Object> first = results.get(0);

                            @SuppressWarnings("unchecked")
                            Map<String, Object> geometry = (Map<String, Object>) first.get("geometry");

                            if (geometry != null) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> location = (Map<String, Object>) geometry.get("location");

                                if (location != null) {
                                    Object latObj = location.get("lat");
                                    Object lngObj = location.get("lng");

                                    if (latObj instanceof Number && lngObj instanceof Number) {
                                        routeStartLat = ((Number) latObj).doubleValue();
                                        routeStartLng = ((Number) lngObj).doubleValue();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao consultar Google Places (parque): " + e.getMessage());
                }

                // PERTO_PRAIA → tentar achar uma “praia” perto
            } else if (request.getFiltros().contains(FiltroRota.PERTO_PRAIA)) {
                try {
                    Map<String, Object> placesResp = placesClient.searchNearby(startLat, startLng, 5000, "praia");

                    if (placesResp != null && placesResp.containsKey("results")) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> results = (List<Map<String, Object>>) placesResp.get("results");

                        if (results != null && !results.isEmpty()) {
                            // default: primeiro resultado
                            Map<String, Object> chosen = results.get(0);

                            // tentar achar algo com "praia" no nome
                            for (Map<String, Object> r : results) {
                                Object nameObj = r.get("name");
                                if (nameObj instanceof String &&
                                        ((String) nameObj).toLowerCase().contains("praia")) {
                                    chosen = r;
                                    break;
                                }
                            }

                            @SuppressWarnings("unchecked")
                            Map<String, Object> geometry = (Map<String, Object>) chosen.get("geometry");

                            if (geometry != null) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> location = (Map<String, Object>) geometry.get("location");

                                if (location != null) {
                                    Object latObj = location.get("lat");
                                    Object lngObj = location.get("lng");

                                    if (latObj instanceof Number && lngObj instanceof Number) {
                                        routeStartLat = ((Number) latObj).doubleValue();
                                        routeStartLng = ((Number) lngObj).doubleValue();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao consultar Google Places (praia): " + e.getMessage());
                }
            }

            if (request.getFiltros() != null && request.getFiltros().contains(FiltroRota.ENSOLARADA)) {
                try {
                    weatherCondition = weatherClient.getWeather(routeStartLat, routeStartLng);
                    if (weatherCondition == null) {
                        weatherCondition = "Unknown";
                    }

                    // OpenWeather "main" costuma ser: Clear, Clouds, Rain, Drizzle, Thunderstorm,
                    // Snow, etc.
                    ensolaradaAtendida = weatherCondition.equalsIgnoreCase("Clear");

                    System.out.println("[Weather] Condição atual: " + weatherCondition +
                            " | filtro ENSOLARADA atendido? " + ensolaradaAtendida);

                } catch (Exception e) {
                    System.out.println("Erro ao consultar OpenWeather: " + e.getMessage());
                    weatherCondition = "Unknown";
                    ensolaradaAtendida = false; // não conseguimos garantir que está ensolarado
                }
            } else {
                // se não pediu ENSOLARADA, podes opcionalmente nem chamar API
                weatherCondition = null; // ou, se quiseres, também podíamos sempre chamar para mostrar ao user
            }

        }

        // 6) Distância alvo
        double distanceKm = (request.getDistanceKm() != null) ? request.getDistanceKm() : 5.0;

        // 7) Gerar pontos da rota via Google Directions (teu client)
        List<RoutePointDTO> pontos = directionsClient.gerarPontosDaRota(
                routeStartLat,
                routeStartLng,
                distanceKm,
                request.getTipoAtividade());

        // 8) Consultar elevação e calcular ganho total de subida
        double totalElevationGain = 0.0;

        if (!pontos.isEmpty()) {
            List<String> latLngPairs = new ArrayList<>();
            for (RoutePointDTO p : pontos) {
                latLngPairs.add(p.getLatitude() + "," + p.getLongitude());
            }

            List<Map<String, Object>> elevationResults = elevationClient.getElevations(latLngPairs);

            boolean first = true;
            double lastElevation = 0.0;

            for (int i = 0; i < pontos.size() && i < elevationResults.size(); i++) {
                RoutePointDTO ponto = pontos.get(i);
                Map<String, Object> elevInfo = elevationResults.get(i);

                Object elevObj = elevInfo.get("elevation");
                double elevation = 0.0;
                if (elevObj instanceof Number) {
                    elevation = ((Number) elevObj).doubleValue();
                }

                ponto.setElevation(elevation);

                if (first) {
                    lastElevation = elevation;
                    first = false;
                } else {
                    double diff = elevation - lastElevation;
                    if (diff > 0) {
                        totalElevationGain += diff;
                    }
                    lastElevation = elevation;
                }
            }
        }

        // 9) Ritmo, duração, calorias (por enquanto fixo)
        double paceMinPerKm = request.getTipoAtividade() == TipoAtividade.CORRIDA ? 6.0 : 12.0;
        double durationMinutes = distanceKm * paceMinPerKm;
        long durationSeconds = Math.round(durationMinutes * 60);

        int estimatedCalories = request.getTipoAtividade() == TipoAtividade.CORRIDA
                ? (int) Math.round(distanceKm * 65)
                : (int) Math.round(distanceKm * 40);

        // 10) Persistir Rota
        Rota rota = new Rota();
        rota.setNome(request.getRouteName());
        rota.setElevacao(totalElevationGain);
        rota = rotaRepository.save(rota);

        // 11) Persistir Locais + LigacaoRotaLocal
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

        // 12) Persistir características da rota (filtros escolhidos)
        if (request.getFiltros() != null) {
            for (FiltroRota filtro : request.getFiltros()) {
                if (filtro == FiltroRota.SEM_FILTRO)
                    continue;

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

        // 13) Persistir Corrida
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

        // 14) MetaUsuario (ligar usuário à corrida)
        MetaUsuario mu = new MetaUsuario();
        mu.setUsuario(usuario);
        mu.setCorrida(corrida);
        mu.setMetaId(null);
        metaUsuarioRepository.save(mu);

        // 15) Montar resposta DTO
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
        resp.setWeatherCondition(weatherCondition);
        resp.setEnsolaradaAtendida(ensolaradaAtendida);

        return resp;
    }

    @Transactional(readOnly = true)
    public List<CorridaHistoricoItemDTO> listarHistoricoPorUsuario(Integer userId) {

        // garantir que o usuário existe
        usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<MetaUsuario> metas = metaUsuarioRepository
                .findByUsuario_IdOrderByCorrida_DataDesc(userId);

        List<CorridaHistoricoItemDTO> historico = new ArrayList<>();

        for (MetaUsuario mu : metas) {
            Corrida c = mu.getCorrida();
            if (c == null)
                continue;

            CorridaHistoricoItemDTO dto = new CorridaHistoricoItemDTO();
            dto.setCorridaId(c.getId());
            dto.setData(c.getData());
            dto.setDistanciaKm(c.getDistancia());

            if (c.getTempo() != null) {
                dto.setDuracaoSegundos((long) c.getTempo().toSecondOfDay());
            }

            dto.setPaceMinPorKm(c.getRitmo());
            dto.setKcal(c.getKcal());

            if (c.getTipo() != null) {
                dto.setTipo(c.getTipo().getNome());
            }

            if (c.getRota() != null) {
                dto.setRouteName(c.getRota().getNome());
                dto.setTotalElevationGain(c.getRota().getElevacao());
            }

            historico.add(dto);
        }

        return historico;
    }

}