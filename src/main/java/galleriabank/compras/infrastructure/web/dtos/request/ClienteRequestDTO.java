package galleriabank.compras.infrastructure.web.dtos.request;

import galleriabank.compras.infrastructure.web.validation.CPF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequestDTO(
        @NotBlank @Size(min = 3) String nome,
        @NotBlank @CPF String cpf,
        String telefone
) {}