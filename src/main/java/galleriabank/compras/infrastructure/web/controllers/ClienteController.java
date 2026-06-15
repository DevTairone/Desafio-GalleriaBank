package galleriabank.compras.infrastructure.web.controllers;

import galleriabank.compras.core.domain.Cliente;
import galleriabank.compras.application.usecases.ClienteUseCase;
import galleriabank.compras.infrastructure.web.dtos.request.ClienteRequestDTO;
import galleriabank.compras.infrastructure.web.dtos.response.ClienteResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteUseCase clienteUseCase;

    public ClienteController(ClienteUseCase clienteUseCase) {
        this.clienteUseCase = clienteUseCase;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(@RequestBody @Valid ClienteRequestDTO dto) {
        Cliente cliente = clienteUseCase.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponseDTO.fromEntity(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ClienteResponseDTO.fromEntity(clienteUseCase.buscarPorId(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}