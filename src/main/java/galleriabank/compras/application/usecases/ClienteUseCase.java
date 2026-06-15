package galleriabank.compras.application.usecases;

import galleriabank.compras.core.domain.Cliente;
import galleriabank.compras.core.ports.ClienteRepositoryPort;
import galleriabank.compras.core.ports.PedidoRepositoryPort;
import galleriabank.compras.infrastructure.web.dtos.request.ClienteRequestDTO;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.infrastructure.web.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ClienteUseCase {

    private final ClienteRepositoryPort clienteRepository;
    private final PedidoRepositoryPort pedidoRepository;

    public ClienteUseCase(ClienteRepositoryPort clienteRepository, PedidoRepositoryPort pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public Cliente cadastrar(ClienteRequestDTO dto) {
        if (clienteRepository.existsByCpf(dto.cpf())) {
            throw new BusinessException("Já existe um cliente cadastrado com este CPF.");
        }
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setTelefone(dto.telefone());
        
        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
    }

    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado.");
        }
        if (pedidoRepository.existsByClienteId(id)) {
            throw new BusinessException("Não é possível excluir um cliente que possui pedidos vinculados.");
        }
        clienteRepository.deleteById(id);
    }
}
