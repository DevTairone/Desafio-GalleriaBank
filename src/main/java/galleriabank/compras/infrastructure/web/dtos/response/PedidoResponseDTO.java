package galleriabank.compras.infrastructure.web.dtos.response;

import galleriabank.compras.core.domain.Pedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        String numero,
        LocalDateTime dataEmissao,
        String descricao,
        Long clienteId,
        String clienteNome,
        List<ProdutoResponseDTO> produtos,
        BigDecimal total
) {
    public static PedidoResponseDTO fromEntity(Pedido pedido) {
        var listaProdutos = pedido.getProdutos().stream()
                .map(p -> new ProdutoResponseDTO(p.getId(), p.getDescricao(), p.getValor()))
                .toList();

        return new PedidoResponseDTO(
                pedido.getNumero(),
                pedido.getDataEmissao(),
                pedido.getDescricao(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                listaProdutos,
                pedido.getTotal()
        );
    }
}