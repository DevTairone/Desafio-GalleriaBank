package galleriabank.compras.infrastructure.web.controllers;

import galleriabank.compras.core.domain.Usuario;
import galleriabank.compras.application.usecases.UsuarioUseCase;
import galleriabank.compras.infrastructure.web.dtos.request.UsuarioRequestDTO;
import galleriabank.compras.infrastructure.web.dtos.response.UsuarioResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    public UsuarioController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioRequestDTO dto) {
        Usuario usuario = usuarioUseCase.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponseDTO.fromEntity(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuarioUseCase.buscarPorId(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerLogico(@PathVariable Long id) {
        usuarioUseCase.removerLogico(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioRequestDTO dto) {
        Usuario usuario = usuarioUseCase.atualizar(id, dto);
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuario));
    }
}