package pt.iade.RunUp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.iade.RunUp.model.Usuario;
import pt.iade.RunUp.model.dto.UsuarioDTO;
import pt.iade.RunUp.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public Usuario cadastrar(@RequestBody UsuarioDTO dto) {
        return usuarioService.criarUsuario(dto);
    }
}
