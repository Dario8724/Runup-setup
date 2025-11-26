package pt.iade.RunUp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iade.RunUp.dto.CorridaResponse;
import pt.iade.RunUp.dto.CreateCorridaRequest;
import pt.iade.RunUp.service.CorridaService;

@RestController
@RequestMapping("/api/corridas")
public class CorridaController {

    private final CorridaService corridaService;

    public CorridaController(CorridaService corridaService) {
        this.corridaService = corridaService;
    }

    @PostMapping
    public ResponseEntity<CorridaResponse> criarCorrida(@RequestBody CreateCorridaRequest request) {
        CorridaResponse response = corridaService.criarCorrida(request);
        return ResponseEntity.ok(response);
    }
}
