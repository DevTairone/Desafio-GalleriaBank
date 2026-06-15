package galleriabank.compras.infrastructure.services;

import galleriabank.compras.core.domain.Cliente;
import galleriabank.compras.infrastructure.persistence.repositories.ClienteRepository;
import galleriabank.compras.infrastructure.persistence.repositories.PedidoRepository;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.infrastructure.web.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    public ClienteService(ClienteRepository clienteRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new BusinessException("Já existe um cliente cadastrado com este CPF.");
        }
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