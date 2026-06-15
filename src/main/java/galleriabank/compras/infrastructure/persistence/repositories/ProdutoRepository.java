package galleriabank.compras.infrastructure.persistence.repositories;

import galleriabank.compras.core.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}