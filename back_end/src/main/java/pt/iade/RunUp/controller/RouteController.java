package pt.iade.RunUp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pt.iade.RunUp.model.dto.RouteRequest;
import pt.iade.RunUp.model.dto.RouteResponse;
import pt.iade.RunUp.service.RouteService;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/generate")
    public RouteResponse generateRoute(@RequestBody RouteRequest request) {

        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'nome' da rota é obrigatório.");
        }

        if (request.getTipo() == null || request.getTipo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tipo deve ser 'corrida' ou 'caminhada'.");
        }

        return routeService.generateRoute(request);
    }
}
