package galleriabank.compras.core.application;

import galleriabank.compras.application.usecases.ProdutoUseCase;
import galleriabank.compras.core.domain.Produto;
import galleriabank.compras.core.ports.PedidoRepositoryPort;
import galleriabank.compras.core.ports.ProdutoRepositoryPort;
import galleriabank.compras.infrastructure.web.dtos.request.ProdutoRequestDTO;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.infrastructure.web.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoUseCaseTest {

    @Mock
    private ProdutoRepositoryPort produtoRepository;

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    private ProdutoUseCase produtoUseCase;
    private ProdutoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        produtoUseCase = new ProdutoUseCase(produtoRepository, pedidoRepository);
        requestDTO = new ProdutoRequestDTO("Notebook", new BigDecimal("4500.00"));
    }

    @Test
    @DisplayName("Deve cadastrar produto com sucesso")
    void deveCadastrarProdutoComSucesso() {
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> {
            Produto produto = invocation.getArgument(0);
            produto.setId(1L);
            return produto;
        });

        Produto salvo = produtoUseCase.cadastrar(requestDTO);

        assertEquals(1L, salvo.getId());
        assertEquals("Notebook", salvo.getDescricao());
        assertEquals(new BigDecimal("4500.00"), salvo.getValor());
    }

    @Test
    @DisplayName("Deve lancar excecao ao buscar produto inexistente")
    void deveLancarExcecaoAoBuscarProdutoInexistente() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> produtoUseCase.buscarPorId(99L));

        assertEquals("Produto não encontrado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    void deveAtualizarProdutoComSucesso() {
        Produto existente = new Produto(1L, "Mouse", new BigDecimal("99.90"));
        ProdutoRequestDTO novoDTO = new ProdutoRequestDTO("Mouse Gamer", new BigDecimal("149.90"));

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Produto atualizado = produtoUseCase.atualizar(1L, novoDTO);

        assertEquals("Mouse Gamer", atualizado.getDescricao());
        assertEquals(new BigDecimal("149.90"), atualizado.getValor());
    }

    @Test
    @DisplayName("Deve lancar excecao ao deletar produto inexistente")
    void deveLancarExcecaoAoDeletarProdutoInexistente() {
        when(produtoRepository.existsById(99L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> produtoUseCase.deletar(99L));

        assertEquals("Produto não encontrado.", exception.getMessage());
        verify(produtoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve lancar excecao ao deletar produto vinculado a pedidos")
    void deveLancarExcecaoAoDeletarProdutoVinculado() {
        when(produtoRepository.existsById(1L)).thenReturn(true);
        when(pedidoRepository.existsByProdutosId(1L)).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> produtoUseCase.deletar(1L));

        assertEquals("Não é possível excluir um produto associado a pedidos existentes.", exception.getMessage());
        verify(produtoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve deletar produto sem vinculo com pedidos")
    void deveDeletarProdutoSemVinculo() {
        when(produtoRepository.existsById(1L)).thenReturn(true);
        when(pedidoRepository.existsByProdutosId(1L)).thenReturn(false);

        produtoUseCase.deletar(1L);

        verify(produtoRepository, times(1)).deleteById(1L);
    }
}

