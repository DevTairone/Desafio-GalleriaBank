package galleriabank.compras.core.ports;

import galleriabank.compras.core.domain.Produto;
import java.util.Optional;

public interface ProdutoRepositoryPort {
    Produto save(Produto produto);
    Optional<Produto> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
}


