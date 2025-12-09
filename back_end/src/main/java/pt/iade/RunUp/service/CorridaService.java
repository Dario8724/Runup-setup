package pt.iade.RunUp.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.iade.RunUp.model.dto.CorridaResponse;
import pt.iade.RunUp.model.dto.CreateCorridaRequest;
import pt.iade.RunUp.model.dto.FinalizarCorridaRequest;
import pt.iade.RunUp.model.dto.RoutePointDTO;
import pt.iade.RunUp.model.entity.*;
import pt.iade.RunUp.repository.*;

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
  private final PredefinedRoutePointsProvider predefinedRoutePointsProvider;

  public CorridaService(
    UsuarioRepository usuarioRepository,
    TipoRepository tipoRepository,
    RotaRepository rotaRepository,
    LocalRepository localRepository,
    LigacaoRotaLocalRepository ligacaoRotaLocalRepository,
    CaracteristicaRepository caracteristicaRepository,
    CaracteristicaRotaRepository caracteristicaRotaRepository,
    CorridaRepository corridaRepository,
    MetaUsuarioRepository metaUsuarioRepository,
    PredefinedRoutePointsProvider predefinedRoutePointsProvider
  ) {
    this.usuarioRepository = usuarioRepository;
    this.tipoRepository = tipoRepository;
    this.rotaRepository = rotaRepository;
    this.localRepository = localRepository;
    this.ligacaoRotaLocalRepository = ligacaoRotaLocalRepository;
    this.caracteristicaRepository = caracteristicaRepository;
    this.caracteristicaRotaRepository = caracteristicaRotaRepository;
    this.corridaRepository = corridaRepository;
    this.metaUsuarioRepository = metaUsuarioRepository;
    this.predefinedRoutePointsProvider = predefinedRoutePointsProvider;
  }

  @Transactional
  public CorridaResponse criarCorrida(CreateCorridaRequest request) {
    Usuario usuario = usuarioRepository
      .findById(request.getUserId())
      .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    Tipo tipo = tipoRepository
      .findFirstByNome(request.getTipoNome())
      .orElseThrow(() ->
        new RuntimeException("Tipo não encontrado: " + request.getTipoNome())
      );

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
        Caracteristica caract = caracteristicaRepository
          .findByTipo(filtro)
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
        double pace =
          (request.getTempoSegundos() / 60.0) / request.getDistanceKm();
        corrida.setRitmo(Math.round(pace * 100.0) / 100.0);
      }
    }

    Integer kcal = request.getKcal();
    if (kcal == null && request.getDistanceKm() != null) {
      kcal = (int) Math.round(request.getDistanceKm() * 60);
    }
    corrida.setKcal(kcal);

    corrida = corridaRepository.save(corrida);

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
    Corrida corrida = corridaRepository
      .findById(corridaId)
      .orElseThrow(() ->
        new RuntimeException("Corrida não encontrada: " + corridaId)
      );

    if (req.getUserId() == null) {
      throw new RuntimeException("UserId é obrigatório para finalizar corrida");
    }

    Integer userId = req.getUserId();

    Usuario usuario = usuarioRepository
      .findById(userId)
      .orElseThrow(() ->
        new RuntimeException("Usuário não encontrado: " + userId)
      );

    // 🔥 SEMPRE garantir que o MetaUsuario fica com o user certo
    MetaUsuario mu = metaUsuarioRepository
      .findFirstByCorrida_Id(corridaId)
      .orElseGet(() -> {
        MetaUsuario novo = new MetaUsuario();
        novo.setCorrida(corrida);
        return novo;
      });

    mu.setUsuario(usuario);
    mu.setMetaId(null);
    metaUsuarioRepository.save(mu);

    // ------- atualiza dados da corrida -------

    Double distKm = req.getDistanciaRealKm();
    Long durSeg = req.getDuracaoSegundos();

    corrida.setDistancia(distKm);
    if (durSeg != null) {
      corrida.setTempo(LocalTime.ofSecondOfDay(durSeg));
    }
    corrida.setKcal(req.getKcal());

    if (distKm != null && distKm > 0 && durSeg != null) {
      double paceMinPorKm = (durSeg / 60.0) / distKm;
      corrida.setRitmo(paceMinPorKm);
    }

    corridaRepository.save(corrida);
  }

  @Transactional
    public CorridaResponse criarCorridaPredefinida(Integer rotaId,
                                                   Integer userId,
                                                   String tipoNome) {

        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userId));

        Rota rota = rotaRepository.findById(rotaId)
                .orElseThrow(() -> new RuntimeException("Rota não encontrada: " + rotaId));

        Tipo tipo = tipoRepository.findFirstByNome(tipoNome)
                .orElseThrow(() -> new RuntimeException("Tipo não encontrado: " + tipoNome));

        // 1) cria Corrida “vazia” (distância real e tempo vêm do app depois)
        Corrida corrida = new Corrida();
        corrida.setData(java.time.LocalDate.now());
        corrida.setTipo(tipo);
        corrida.setRota(rota);
        corrida = corridaRepository.save(corrida);

        // 2) garante que a corrida está associada ao utilizador certo
        MetaUsuario mu = new MetaUsuario();
        mu.setUsuario(usuario);
        mu.setCorrida(corrida);
        mu.setMetaId(null);
        metaUsuarioRepository.save(mu);

        // 3) buscar os pontos predefinidos (polylines) em memória
        var pontos = predefinedRoutePointsProvider.getPointsForRota(rotaId);

        // 4) montar resposta no mesmo formato usado pelo gerarCorrida
        CorridaResponse response = new CorridaResponse();
        response.setCorridaId(corrida.getId());
        response.setUserId(usuario.getId());
        response.setRouteName(rota.getNome());
        response.setTipoNome(tipo.getNome());
        response.setDistanceKm(null); // pode ficar null; app mede real
        response.setData(corrida.getData());
        response.setElevacaoTotal(rota.getElevacao());
        response.setKcal(null);
        response.setPontos(pontos);
        response.setFiltros(null);

        return response;
    }
}
