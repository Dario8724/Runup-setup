package pt.iade.RunUp.service;

import org.springframework.stereotype.Service;
import pt.iade.RunUp.model.dto.PredefinedRouteDto;
import pt.iade.RunUp.model.entity.Rota;
import pt.iade.RunUp.repository.RotaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PredefinedRouteService {

    private static final int ID_BELEM_4K = 37;
    private static final int ID_LISBOA_21K_RIO = 38;
    private static final int ID_LISBOA_21K_CENTRO = 39;

    private final RotaRepository rotaRepository;

    public PredefinedRouteService(RotaRepository rotaRepository) {
        this.rotaRepository = rotaRepository;
    }

    public List<PredefinedRouteDto> getAll() {
        List<PredefinedRouteDto> result = new ArrayList<>();

        addIfExists(
                result,
                ID_BELEM_4K,
                4.0,
                "Caminhada",
                "Fácil",
                "Praça do Império · Torre de Belém · Padrão dos Descobrimentos · Museu da Marinha"
        );

        addIfExists(
                result,
                ID_LISBOA_21K_RIO,
                21.0,
                "Corrida",
                "Intermédio",
                "Algés · Belém · Cais do Sodré · Terreiro do Paço · Santa Apolónia"
        );

        addIfExists(
                result,
                ID_LISBOA_21K_CENTRO,
                21.0,
                "Corrida",
                "Intermédio",
                "Fontes Pereira de Melo · Saldanha · Campo Grande · Alameda Universidade"
        );

        return result;
    }

    private void addIfExists(
            List<PredefinedRouteDto> list,
            int rotaId,
            double distanciaKm,
            String tipo,
            String dificuldade,
            String descricao
    ) {
        rotaRepository.findById(rotaId).ifPresent(rota -> {
            PredefinedRouteDto dto = new PredefinedRouteDto();
            dto.setRotaId(rota.getId());
            dto.setNome(rota.getNome());
            dto.setDistanciaKm(distanciaKm);
            dto.setTipo(tipo);
            dto.setDificuldade(dificuldade);
            dto.setDescricao(descricao);
            list.add(dto);
        });
    }
}