package galleriabank.compras.infrastructure.web.dtos.response;

import galleriabank.compras.core.domain.Cliente;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        String telefone
) {
    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getTelefone()
        );
    }
}