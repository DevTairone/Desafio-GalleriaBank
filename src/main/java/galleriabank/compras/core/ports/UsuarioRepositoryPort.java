package galleriabank.compras.core.ports;

import galleriabank.compras.core.domain.Usuario;
import java.util.Optional;

public interface UsuarioRepositoryPort {
    Optional<Usuario> findByIdAndExcluidoFalse(Long id);
    boolean existsByLogin(String login);
    Usuario save(Usuario usuario);
}

