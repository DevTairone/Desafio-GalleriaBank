package galleriabank.compras.core.ports;

import galleriabank.compras.core.domain.Pedido;
import java.util.Optional;

public interface PedidoRepositoryPort {
    Pedido save(Pedido pedido);
    Optional<Pedido> findById(Long id);
    boolean existsByClienteId(Long clienteId);
    boolean existsByProdutosId(Long produtoId);
}

