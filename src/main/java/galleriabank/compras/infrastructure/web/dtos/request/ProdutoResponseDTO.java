package galleriabank.compras.infrastructure.web.dtos.request;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor
) {}