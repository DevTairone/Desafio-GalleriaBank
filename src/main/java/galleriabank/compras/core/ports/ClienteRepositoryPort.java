package galleriabank.compras.core.ports;

import galleriabank.compras.core.domain.Cliente;
import java.util.Optional;

public interface ClienteRepositoryPort {
    boolean existsByCpf(String cpf);
    Cliente save(Cliente cliente);
    Optional<Cliente> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
}


