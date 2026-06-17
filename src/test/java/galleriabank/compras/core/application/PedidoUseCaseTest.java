package galleriabank.compras.core.application;

import galleriabank.compras.application.usecases.PedidoUseCase;
import galleriabank.compras.core.domain.Cliente;
import galleriabank.compras.core.domain.Pedido;
import galleriabank.compras.core.domain.Produto;
import galleriabank.compras.core.ports.ClienteRepositoryPort;
import galleriabank.compras.core.ports.PedidoRepositoryPort;
import galleriabank.compras.core.ports.ProdutoRepositoryPort;
import galleriabank.compras.infrastructure.web.dtos.request.PedidoRequestDTO;
import galleriabank.compras.infrastructure.web.dtos.response.PedidoResponseDTO;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.infrastructure.web.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoUseCaseTest {

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @Mock
    private ClienteRepositoryPort clienteRepository;

    @Mock
    private ProdutoRepositoryPort produtoRepository;

    private PedidoUseCase pedidoUseCase;
    private Cliente cliente;
    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setUp() {
        pedidoUseCase = new PedidoUseCase(pedidoRepository, clienteRepository, produtoRepository);
        cliente = new Cliente(1L, "Cliente A", "52998224725", "11999999999");
        produto1 = new Produto(10L, "Produto 1", new BigDecimal("10.00"));
        produto2 = new Produto(11L, "Produto 2", new BigDecimal("25.50"));
    }

    @Test
    @DisplayName("Deve lancar excecao quando lista de produtos for nula")
    void deveLancarExcecaoQuandoProdutosIdsForNulo() {
        PedidoRequestDTO dto = new PedidoRequestDTO("Pedido X", 1L, null);

        BusinessException exception = assertThrows(BusinessException.class, () -> pedidoUseCase.criarPedido(dto));

        assertEquals("O pedido deve conter pelo menos um produto.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar excecao quando lista de produtos estiver vazia")
    void deveLancarExcecaoQuandoProdutosIdsForVazio() {
        PedidoRequestDTO dto = new PedidoRequestDTO("Pedido X", 1L, List.of());

        BusinessException exception = assertThrows(BusinessException.class, () -> pedidoUseCase.criarPedido(dto));

        assertEquals("O pedido deve conter pelo menos um produto.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar excecao ao criar pedido com cliente inexistente")
    void deveLancarExcecaoQuandoClienteNaoExiste() {
        PedidoRequestDTO dto = new PedidoRequestDTO("Pedido X", 999L, List.of(10L));
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> pedidoUseCase.criarPedido(dto));

        assertEquals("Cliente não encontrado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar excecao ao criar pedido com produto inexistente")
    void deveLancarExcecaoQuandoProdutoNaoExiste() {
        PedidoRequestDTO dto = new PedidoRequestDTO("Pedido X", 1L, List.of(99L));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> pedidoUseCase.criarPedido(dto));

        assertEquals("Produto não encontrado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso e gerar numero quando vier nulo")
    void deveCriarPedidoComSucessoGerandoNumero() {
        PedidoRequestDTO dto = new PedidoRequestDTO("Pedido completo", 1L, List.of(10L, 11L));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto1));
        when(produtoRepository.findById(11L)).thenReturn(Optional.of(produto2));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedido = invocation.getArgument(0);
            if (pedido.getId() == null) {
                pedido.setId(77L);
                pedido.setDataEmissao(LocalDateTime.now());
            }
            return pedido;
        });

        PedidoResponseDTO response = pedidoUseCase.criarPedido(dto);

        assertEquals("PED-77", response.numero());
        assertEquals("Pedido completo", response.descricao());
        assertEquals(cliente.getId(), response.clienteId());
        assertEquals(2, response.produtos().size());
        assertEquals(new BigDecimal("35.50"), response.total());
        verify(pedidoRepository, times(2)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Nao deve salvar duas vezes quando numero ja existir")
    void naoDeveSalvarDuasVezesQuandoNumeroJaExiste() {
        PedidoRequestDTO dto = new PedidoRequestDTO("Pedido sem segundo save", 1L, List.of(10L));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto1));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedido = invocation.getArgument(0);
            pedido.setId(88L);
            pedido.setNumero("PED-88");
            pedido.setDataEmissao(LocalDateTime.now());
            return pedido;
        });

        PedidoResponseDTO response = pedidoUseCase.criarPedido(dto);

        assertEquals("PED-88", response.numero());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve buscar pedido por id com sucesso")
    void deveBuscarPedidoPorIdComSucesso() {
        Pedido pedido = new Pedido();
        pedido.setId(10L);
        pedido.setNumero("PED-10");
        pedido.setDataEmissao(LocalDateTime.now());
        pedido.setDescricao("Pedido buscado");
        pedido.setCliente(cliente);
        pedido.setProdutos(List.of(produto1));

        when(pedidoRepository.findById(10L)).thenReturn(Optional.of(pedido));

        PedidoResponseDTO response = pedidoUseCase.buscarPorId(10L);

        assertEquals("PED-10", response.numero());
        assertEquals("Pedido buscado", response.descricao());
        assertEquals(1, response.produtos().size());
        assertTrue(response.total().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Deve lancar excecao ao buscar pedido inexistente")
    void deveLancarExcecaoAoBuscarPedidoInexistente() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> pedidoUseCase.buscarPorId(999L));

        assertEquals("Pedido não encontrado.", exception.getMessage());
    }
}

