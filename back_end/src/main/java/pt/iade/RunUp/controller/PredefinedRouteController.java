package pt.iade.RunUp.controller;

import org.springframework.web.bind.annotation.*;
import pt.iade.RunUp.model.dto.CorridaResponse;
import pt.iade.RunUp.model.dto.PredefinedRouteDto;
import pt.iade.RunUp.service.CorridaService;
import pt.iade.RunUp.service.PredefinedRouteService;

import java.util.List;

@RestController
@RequestMapping("/api/rotas/predefinidas")
@CrossOrigin(origins = "*")
public class PredefinedRouteController {

    private final PredefinedRouteService predefinedRouteService;
    private final CorridaService corridaService;

    public PredefinedRouteController(
            PredefinedRouteService predefinedRouteService,
            CorridaService corridaService
    ) {
        this.predefinedRouteService = predefinedRouteService;
        this.corridaService = corridaService;
    }

    // GET /api/rotas/predefinidas
    @GetMapping
    public List<PredefinedRouteDto> listAll() {
        return predefinedRouteService.getAll();
    }

    // POST /api/rotas/predefinidas/{rotaId}/iniciar?userId=...&tipo=Corrida
    @PostMapping("/{rotaId}/iniciar")
    public CorridaResponse startPredefinedRoute(
            @PathVariable Integer rotaId,
            @RequestParam("userId") Integer userId,
            @RequestParam(name = "tipo", defaultValue = "Corrida") String tipoNome
    ) {
        return corridaService.criarCorridaPredefinida(rotaId, userId, tipoNome);
    }
}
