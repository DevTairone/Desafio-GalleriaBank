package galleriabank.compras.infrastructure.web.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank @Size(min = 3) String nome,
        @NotBlank String login,
        @NotBlank String senha
) {}