package pt.iade.RunUp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iade.RunUp.model.dto.CorridaResponse;
import pt.iade.RunUp.model.dto.CreateCorridaRequest;
import pt.iade.RunUp.model.dto.FinalizarCorridaRequest;
import pt.iade.RunUp.service.CorridaService;

@RestController
@RequestMapping("/api/corridas")
public class CorridaController {

    private final CorridaService corridaService;

    public CorridaController(CorridaService corridaService) {
        this.corridaService = corridaService;
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarCorrida(
        @PathVariable Integer id, 
        @RequestBody FinalizarCorridaRequest body) {

        corridaService.finalizarCorrida(id, body);
        return ResponseEntity.ok().build();
    }
}
