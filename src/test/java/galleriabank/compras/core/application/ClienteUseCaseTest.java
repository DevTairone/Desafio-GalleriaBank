package galleriabank.compras.core.application;

import galleriabank.compras.application.usecases.ClienteUseCase;
import galleriabank.compras.core.domain.Cliente;
import galleriabank.compras.core.ports.ClienteRepositoryPort;
import galleriabank.compras.core.ports.PedidoRepositoryPort;
import galleriabank.compras.infrastructure.web.dtos.request.ClienteRequestDTO;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.infrastructure.web.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteUseCaseTest {

    @Mock
    private ClienteRepositoryPort clienteRepository;

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    private ClienteUseCase clienteUseCase;
    private ClienteRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        clienteUseCase = new ClienteUseCase(clienteRepository, pedidoRepository);
        requestDTO = new ClienteRequestDTO("Tairone", "12345678909", "11999999999");
    }

    @Test
    @DisplayName("Deve cadastrar cliente com sucesso quando CPF nao existir")
    void deveCadastrarClienteComSucesso() {
        when(clienteRepository.existsByCpf(requestDTO.cpf())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente cliente = invocation.getArgument(0);
            cliente.setId(1L);
            return cliente;
        });

        Cliente clienteSalvo = clienteUseCase.cadastrar(requestDTO);

        assertEquals(1L, clienteSalvo.getId());
        assertEquals("Tairone", clienteSalvo.getNome());
        assertEquals("12345678909", clienteSalvo.getCpf());
        assertEquals("11999999999", clienteSalvo.getTelefone());
        verify(clienteRepository, times(1)).existsByCpf(requestDTO.cpf());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lancar excecao ao cadastrar com CPF ja existente")
    void deveLancarExcecaoQuandoCpfJaExiste() {
        when(clienteRepository.existsByCpf(requestDTO.cpf())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> clienteUseCase.cadastrar(requestDTO));

        assertEquals("Já existe um cliente cadastrado com este CPF.", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar cliente por id com sucesso")
    void deveBuscarPorIdComSucesso() {
        Cliente cliente = new Cliente(1L, "Tairone", "12345678909", "11999999999");
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteUseCase.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Tairone", resultado.getNome());
    }

    @Test
    @DisplayName("Deve lancar excecao ao buscar cliente inexistente")
    void deveLancarExcecaoAoBuscarClienteInexistente() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> clienteUseCase.buscarPorId(99L));

        assertEquals("Cliente não encontrado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar excecao ao deletar cliente inexistente")
    void deveLancarExcecaoAoDeletarClienteInexistente() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> clienteUseCase.deletar(99L));

        assertEquals("Cliente não encontrado.", exception.getMessage());
        verify(pedidoRepository, never()).existsByClienteId(any());
        verify(clienteRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve lancar excecao ao deletar cliente com pedidos vinculados")
    void deveLancarExcecaoAoDeletarClienteComPedidos() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(pedidoRepository.existsByClienteId(1L)).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> clienteUseCase.deletar(1L));

        assertEquals("Não é possível excluir um cliente que possui pedidos vinculados.", exception.getMessage());
        verify(clienteRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve deletar cliente sem pedidos vinculados")
    void deveDeletarClienteSemPedidos() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(pedidoRepository.existsByClienteId(1L)).thenReturn(false);

        clienteUseCase.deletar(1L);

        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve atualizar cliente com sucesso")
    void deveAtualizarClienteComSucesso() {
        Cliente clienteExistente = new Cliente(1L, "Nome Antigo", "12345678909", "11000000000");
        ClienteRequestDTO novoDTO = new ClienteRequestDTO("Novo Nome", "98765432100", "11988887777");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cliente atualizado = clienteUseCase.atualizar(1L, novoDTO);

        assertEquals("Novo Nome", atualizado.getNome());
        assertEquals("98765432100", atualizado.getCpf());
        assertEquals("11988887777", atualizado.getTelefone());
    }

    @Test
    @DisplayName("Deve lancar excecao ao atualizar cliente inexistente")
    void deveLancarExcecaoAoAtualizarClienteInexistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> clienteUseCase.atualizar(1L, requestDTO));

        assertEquals("Cliente não encontrado.", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }
}

