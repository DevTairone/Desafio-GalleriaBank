package galleriabank.compras.infrastructure.persistence.adapters;

import galleriabank.compras.core.domain.Cliente;
import galleriabank.compras.core.domain.Pedido;
import galleriabank.compras.core.domain.Produto;
import galleriabank.compras.core.domain.Usuario;
import galleriabank.compras.infrastructure.persistence.repositories.ClienteRepository;
import galleriabank.compras.infrastructure.persistence.repositories.PedidoRepository;
import galleriabank.compras.infrastructure.persistence.repositories.ProdutoRepository;
import galleriabank.compras.infrastructure.persistence.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RepositoryAdaptersDelegationTest {

    @Test
    @DisplayName("UsuarioRepositoryAdapter deve delegar metodos para UsuarioRepository")
    void usuarioAdapterDeveDelegarMetodos() {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        UsuarioRepositoryAdapter adapter = new UsuarioRepositoryAdapter(repository);
        Usuario usuario = new Usuario(1L, "User", "login", "senha", false);

        when(repository.findByIdAndExcluidoFalse(1L)).thenReturn(Optional.of(usuario));
        when(repository.existsByLogin("login")).thenReturn(true);
        when(repository.save(usuario)).thenReturn(usuario);

        Optional<Usuario> encontrado = adapter.findByIdAndExcluidoFalse(1L);
        boolean existeLogin = adapter.existsByLogin("login");
        Usuario salvo = adapter.save(usuario);

        assertTrue(encontrado.isPresent());
        assertTrue(existeLogin);
        assertEquals(usuario, salvo);
        verify(repository, times(1)).findByIdAndExcluidoFalse(1L);
        verify(repository, times(1)).existsByLogin("login");
        verify(repository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("ClienteRepositoryAdapter deve delegar metodos para ClienteRepository")
    void clienteAdapterDeveDelegarMetodos() {
        ClienteRepository repository = mock(ClienteRepository.class);
        ClienteRepositoryAdapter adapter = new ClienteRepositoryAdapter(repository);
        Cliente cliente = new Cliente(1L, "Cliente", "52998224725", "11999999999");

        when(repository.existsByCpf(cliente.getCpf())).thenReturn(true);
        when(repository.save(cliente)).thenReturn(cliente);
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        when(repository.existsById(1L)).thenReturn(true);

        boolean existeCpf = adapter.existsByCpf(cliente.getCpf());
        Cliente salvo = adapter.save(cliente);
        Optional<Cliente> encontrado = adapter.findById(1L);
        boolean existeId = adapter.existsById(1L);
        adapter.deleteById(1L);

        assertTrue(existeCpf);
        assertEquals(cliente, salvo);
        assertTrue(encontrado.isPresent());
        assertTrue(existeId);
        verify(repository, times(1)).existsByCpf(cliente.getCpf());
        verify(repository, times(1)).save(cliente);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("ProdutoRepositoryAdapter deve delegar metodos para ProdutoRepository")
    void produtoAdapterDeveDelegarMetodos() {
        ProdutoRepository repository = mock(ProdutoRepository.class);
        ProdutoRepositoryAdapter adapter = new ProdutoRepositoryAdapter(repository);
        Produto produto = new Produto(1L, "Produto", new BigDecimal("99.90"));

        when(repository.save(produto)).thenReturn(produto);
        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        when(repository.existsById(1L)).thenReturn(true);

        Produto salvo = adapter.save(produto);
        Optional<Produto> encontrado = adapter.findById(1L);
        boolean existeId = adapter.existsById(1L);
        adapter.deleteById(1L);

        assertEquals(produto, salvo);
        assertTrue(encontrado.isPresent());
        assertTrue(existeId);
        verify(repository, times(1)).save(produto);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("PedidoRepositoryAdapter deve delegar metodos para PedidoRepository")
    void pedidoAdapterDeveDelegarMetodos() {
        PedidoRepository repository = mock(PedidoRepository.class);
        PedidoRepositoryAdapter adapter = new PedidoRepositoryAdapter(repository);
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        when(repository.save(pedido)).thenReturn(pedido);
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));
        when(repository.existsByClienteId(10L)).thenReturn(true);
        when(repository.existsByProdutosId(20L)).thenReturn(true);

        Pedido salvo = adapter.save(pedido);
        Optional<Pedido> encontrado = adapter.findById(1L);
        boolean existeCliente = adapter.existsByClienteId(10L);
        boolean existeProduto = adapter.existsByProdutosId(20L);

        assertEquals(pedido, salvo);
        assertTrue(encontrado.isPresent());
        assertTrue(existeCliente);
        assertTrue(existeProduto);
        verify(repository, times(1)).save(pedido);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).existsByClienteId(10L);
        verify(repository, times(1)).existsByProdutosId(20L);
    }
}

