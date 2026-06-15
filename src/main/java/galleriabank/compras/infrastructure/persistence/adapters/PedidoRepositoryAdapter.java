package galleriabank.compras.infrastructure.persistence.adapters;

import galleriabank.compras.core.domain.Pedido;
import galleriabank.compras.core.ports.PedidoRepositoryPort;
import galleriabank.compras.infrastructure.persistence.repositories.PedidoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final PedidoRepository pedidoRepository;

    public PedidoRepositoryAdapter(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public boolean existsByClienteId(Long clienteId) {
        return pedidoRepository.existsByClienteId(clienteId);
    }

    @Override
    public boolean existsByProdutosId(Long produtoId) {
        return pedidoRepository.existsByProdutosId(produtoId);
    }
}