package galleriabank.compras.application.usecases;

import galleriabank.compras.core.domain.Produto;
import galleriabank.compras.core.ports.PedidoRepositoryPort;
import galleriabank.compras.infrastructure.web.dtos.request.ProdutoRequestDTO;
import galleriabank.compras.core.ports.ProdutoRepositoryPort;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.infrastructure.web.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProdutoUseCase {

    private final ProdutoRepositoryPort produtoRepository;
    private final PedidoRepositoryPort pedidoRepository;

    public ProdutoUseCase(ProdutoRepositoryPort produtoRepository, PedidoRepositoryPort pedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public Produto cadastrar(ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setDescricao(dto.descricao());
        produto.setValor(dto.valor());
        
        return produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public Produto atualizar(Long id,ProdutoRequestDTO dto) {
        Produto produto = buscarPorId(id);
        produto.setDescricao(dto.descricao());
        produto.setValor(dto.valor());
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
