package pt.iade.RunUp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iade.RunUp.dto.CorridaGeradaResponse;
import pt.iade.RunUp.dto.GenerateCorridaRequest;
import pt.iade.RunUp.service.CorridaGenerationService;

@RestController
@RequestMapping("/api/corridas")
public class CorridaGenerationController {

    private final CorridaGenerationService corridaGenerationService;

    public CorridaGenerationController(CorridaGenerationService corridaGenerationService) {
        this.corridaGenerationService = corridaGenerationService;
    }

    @PostMapping("/gerar")
    public ResponseEntity<CorridaGeradaResponse> gerarCorrida(@RequestBody GenerateCorridaRequest request) {
        CorridaGeradaResponse response = corridaGenerationService.gerarCorrida(request);
        return ResponseEntity.ok(response);
    }
}
