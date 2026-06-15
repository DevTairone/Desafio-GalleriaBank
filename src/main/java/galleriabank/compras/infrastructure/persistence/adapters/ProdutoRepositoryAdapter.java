package galleriabank.compras.infrastructure.persistence.adapters;

import galleriabank.compras.core.domain.Produto;
import galleriabank.compras.core.ports.ProdutoRepositoryPort;
import galleriabank.compras.infrastructure.persistence.repositories.ProdutoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProdutoRepositoryAdapter implements ProdutoRepositoryPort {

    private final ProdutoRepository produtoRepository;

    public ProdutoRepositoryAdapter(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Override
    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return produtoRepository.existsById(id);
    }
    @Override
    public void deleteById(Long id) {
        produtoRepository.deleteById(id);
    }
}