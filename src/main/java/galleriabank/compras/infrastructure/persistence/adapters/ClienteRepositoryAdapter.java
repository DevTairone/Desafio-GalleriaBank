package galleriabank.compras.infrastructure.persistence.adapters;

import galleriabank.compras.core.domain.Cliente;
import galleriabank.compras.core.ports.ClienteRepositoryPort;
import galleriabank.compras.infrastructure.persistence.repositories.ClienteRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final ClienteRepository clienteRepository;

    public ClienteRepositoryAdapter(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return clienteRepository.existsByCpf(cpf);
    }

    @Override
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }
}