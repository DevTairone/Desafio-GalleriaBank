package galleriabank.compras.infrastructure.web.dtos.response;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor
){}