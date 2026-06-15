package galleriabank.compras.infrastructure.persistence.repositories;

import galleriabank.compras.core.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    boolean existsByClienteId(Long clienteId);

    boolean existsByProdutosId(Long produtoId);
}