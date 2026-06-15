package galleriabank.compras.infrastructure.persistence.adapters;

import galleriabank.compras.core.domain.Usuario;
import galleriabank.compras.core.ports.UsuarioRepositoryPort;
import galleriabank.compras.infrastructure.persistence.repositories.UsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioRepository usuarioRepository;

    public UsuarioRepositoryAdapter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<Usuario> findByIdAndExcluidoFalse(Long id) {
        return usuarioRepository.findByIdAndExcluidoFalse(id);
    }

    @Override
    public boolean existsByLogin(String login) {
        return usuarioRepository.existsByLogin(login);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}