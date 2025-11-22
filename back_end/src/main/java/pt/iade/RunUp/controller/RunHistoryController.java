package pt.iade.RunUp.controller;

import org.springframework.web.bind.annotation.*;
import pt.iade.RunUp.model.Route;
import pt.iade.RunUp.model.RunHistory;
import pt.iade.RunUp.model.dto.RunHistoryRequestDTO;
import pt.iade.RunUp.model.dto.RunHistoryResponseDTO;
import pt.iade.RunUp.repository.RouteRepository;
import pt.iade.RunUp.service.RunHistoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "*")
public class RunHistoryController {

    private final RunHistoryService runHistoryService;
    private final RouteRepository routeRepository;

    public RunHistoryController(
            RunHistoryService runHistoryService,
            RouteRepository routeRepository
    ) {
        this.runHistoryService = runHistoryService;
        this.routeRepository = routeRepository;
    }

    // ============================================================
    // GET - Retorna histórico por usuário
    // ============================================================
    @GetMapping("/{userId}")
    public List<RunHistoryResponseDTO> getUserHistory(@PathVariable Long userId) {
        return runHistoryService.getHistoryByUser(userId).stream()
                .map(history -> {

                    // Buscar o nome da rota pelo ID
                    String nomeRota = routeRepository.findById(history.getRotaId())
                            .map(Route::getNome)
                            .orElse("Rota não encontrada");

                    return RunHistoryResponseDTO.builder()
                            .id(history.getId())
                            .dataCorrida(history.getDataCorrida().toString())
                            .local(nomeRota)
                            .distanciaKm(history.getDistanciaKm())
                            .duracao(formatDuration(history.getDuracaoMinutos()))
                            .tipo(history.getTipo())
                            .calorias(history.getCalorias())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // Helper para formatar duração
    private String formatDuration(Double duracaoMinutos) {
        if (duracaoMinutos == null) return "00:00";
        int totalSegundos = (int) (duracaoMinutos * 60);
        int minutos = totalSegundos / 60;
        int segundos = totalSegundos % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    // ============================================================
    // POST - Salvar nova corrida
    // ============================================================
    @PostMapping
    public RunHistoryResponseDTO saveRun(@RequestBody RunHistoryRequestDTO dto) {

        RunHistory saved = runHistoryService.saveRun(dto);

        String nomeRota = routeRepository.findById(dto.getRotaId())
                .map(Route::getNome)
                .orElse("Rota não encontrada");

        return RunHistoryResponseDTO.builder()
                .id(saved.getId())
                .dataCorrida(saved.getDataCorrida().toString())
                .local(nomeRota)
                .distanciaKm(saved.getDistanciaKm())
                .duracao(formatDuration(saved.getDuracaoMinutos()))
                .tipo(saved.getTipo())
                .calorias(saved.getCalorias())
                .build();
    }
}
