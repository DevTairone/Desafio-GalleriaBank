package galleriabank.compras.application.usecases;

import galleriabank.compras.core.domain.Cliente;
import galleriabank.compras.core.domain.Pedido;
import galleriabank.compras.core.domain.Produto;
import galleriabank.compras.core.ports.ClienteRepositoryPort;
import galleriabank.compras.core.ports.PedidoRepositoryPort;
import galleriabank.compras.core.ports.ProdutoRepositoryPort;
import galleriabank.compras.infrastructure.web.dtos.request.PedidoRequestDTO;
import galleriabank.compras.infrastructure.web.dtos.response.PedidoResponseDTO;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.infrastructure.web.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoUseCase {

    private final PedidoRepositoryPort pedidoRepository;
    private final ClienteRepositoryPort clienteRepository;
    private final ProdutoRepositoryPort produtoRepository;

    public PedidoUseCase(PedidoRepositoryPort pedidoRepository, ClienteRepositoryPort clienteRepository, ProdutoRepositoryPort produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        if (dto.produtosIds() == null || dto.produtosIds().isEmpty()) {
            throw new BusinessException("O pedido deve conter pelo menos um produto.");
        }

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        List<Produto> produtos = dto.produtosIds().stream()
                .map(id -> produtoRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado.")))
                .toList();

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setProdutos(produtos);
        pedido.setDescricao(dto.descricao());

        if (dto.numero() != null && !dto.numero().isBlank()) {
            pedido.setNumero(dto.numero());
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        if (pedidoSalvo.getNumero() == null) {
            pedidoSalvo.setNumero("PED-" + pedidoSalvo.getId());
            pedidoSalvo = pedidoRepository.save(pedidoSalvo);
        }

        return PedidoResponseDTO.fromEntity(pedidoSalvo);
    }

    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado."));
        return PedidoResponseDTO.fromEntity(pedido);
    }
}
