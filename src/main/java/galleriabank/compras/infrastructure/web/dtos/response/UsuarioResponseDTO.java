package galleriabank.compras.infrastructure.web.dtos.response;

import galleriabank.compras.core.domain.Usuario;

public record UsuarioResponseDTO(Long id, String nome, String login) {
    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getLogin());
    }
}