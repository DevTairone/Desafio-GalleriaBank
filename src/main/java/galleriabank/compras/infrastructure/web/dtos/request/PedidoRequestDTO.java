package galleriabank.compras.infrastructure.web.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoRequestDTO(
        String descricao,
        @NotNull Long clienteId,
        @NotEmpty List<Long> produtosIds
) {}