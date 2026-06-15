package galleriabank.compras.infrastructure.services;

import galleriabank.compras.core.domain.Produto;
import galleriabank.compras.infrastructure.persistence.repositories.PedidoRepository;
import galleriabank.compras.infrastructure.persistence.repositories.ProdutoRepository;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.infrastructure.web.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    public ProdutoService(ProdutoRepository produtoRepository, PedidoRepository pedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public Produto cadastrar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));
    }

    @Transactional
    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado.");
        }
        if (pedidoRepository.existsByProdutosId(id)) {
            throw new BusinessException("Não é possível excluir um produto associado a pedidos existentes.");
        }
        produtoRepository.deleteById(id);
    }
}