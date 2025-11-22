package pt.iade.RunUp.service;

import org.springframework.stereotype.Service;
import pt.iade.RunUp.model.RunHistory;
import pt.iade.RunUp.model.dto.RunHistoryRequestDTO;
import pt.iade.RunUp.repository.RunHistoryRepository;

import java.util.List;

@Service
public class RunHistoryService {

    private final RunHistoryRepository repository;

    public RunHistoryService(RunHistoryRepository repository) {
        this.repository = repository;
    }

    public List<RunHistory> getHistoryByUser(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    // Novo método compatível com o Controller
    public RunHistory saveRun(RunHistoryRequestDTO dto) {

        RunHistory run = RunHistory.builder()
                .usuarioId(dto.getUsuarioId())
                .rotaId(dto.getRotaId())
                .dataCorrida(dto.getDataCorrida())
                .distanciaKm(dto.getDistanciaKm())
                .duracaoMinutos(dto.getDuracaoMinutos())
                .tipo(dto.getTipo())
                .calorias(dto.getCalorias())
                .build();

        return repository.save(run);
    }
}
