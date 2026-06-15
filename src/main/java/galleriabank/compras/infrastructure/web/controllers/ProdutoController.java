package galleriabank.compras.infrastructure.web.controllers;

import galleriabank.compras.core.domain.Produto;
import galleriabank.compras.application.usecases.ProdutoUseCase;
import galleriabank.compras.infrastructure.web.dtos.request.ProdutoRequestDTO;
import galleriabank.compras.infrastructure.web.dtos.response.ProdutoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoUseCase produtoUseCase;

    public ProdutoController(ProdutoUseCase produtoUseCase) {
        this.produtoUseCase = produtoUseCase;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@RequestBody @Valid ProdutoRequestDTO dto) {
        Produto produto = produtoUseCase.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ProdutoResponseDTO(produto.getId(), produto.getDescricao(), produto.getValor()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        Produto p = produtoUseCase.buscarPorId(id);
        return ResponseEntity.ok(new ProdutoResponseDTO(p.getId(), p.getDescricao(), p.getValor()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO dto) {
        Produto produto = produtoUseCase.atualizar(id, dto);
        return ResponseEntity.ok(new ProdutoResponseDTO(produto.getId(), produto.getDescricao(), produto.getValor()));
    }
}