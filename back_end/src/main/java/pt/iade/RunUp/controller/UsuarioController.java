package pt.iade.RunUp.controller;

import org.springframework.web.bind.annotation.*;
import pt.iade.RunUp.model.dto.UserStatsDto;
import pt.iade.RunUp.service.UsuarioService;
import pt.iade.RunUp.model.dto.WeeklyStatsDto;
import pt.iade.RunUp.model.dto.PersonalRecordDto;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}/stats")
    public UserStatsDto getUserStats(@PathVariable Integer id) {
        return usuarioService.getUserStats(id);
    }

    @GetMapping("/{id}/weekly-stats")
    public WeeklyStatsDto getWeeklyStats(@PathVariable Integer id) {
        return usuarioService.getWeeklyStats(id);
    }

    @GetMapping("/{id}/records/distance")
    public PersonalRecordDto getMaiorDistancia(@PathVariable Integer id) {
        return usuarioService.getMaiorDistancia(id);
    }
}