package galleriabank.compras.infrastructure.services;

import galleriabank.compras.core.domain.Cliente;
import galleriabank.compras.core.domain.Pedido;
import galleriabank.compras.core.domain.Produto;
import galleriabank.compras.infrastructure.persistence.repositories.PedidoRepository;
import galleriabank.compras.infrastructure.web.dtos.request.PedidoRequestDTO;
import galleriabank.compras.infrastructure.web.dtos.response.PedidoResponseDTO;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.infrastructure.web.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    public PedidoService(PedidoRepository pedidoRepository, ClienteService clienteService, ProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        if (dto.produtosIds() == null || dto.produtosIds().isEmpty()) {
            throw new BusinessException("O pedido deve conter pelo menos um produto.");
        }

        Cliente cliente = clienteService.buscarPorId(dto.clienteId());

        List<Produto> produtos = dto.produtosIds().stream()
                .map(produtoService::buscarPorId)
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