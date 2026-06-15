package galleriabank.compras.infrastructure.services;

import galleriabank.compras.core.domain.Usuario;
import galleriabank.compras.infrastructure.persistence.repositories.UsuarioRepository;
import galleriabank.compras.infrastructure.web.dtos.request.UsuarioRequestDTO;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.infrastructure.web.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario cadastrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByLogin(dto.login())) {
            throw new BusinessException("Este login já está em uso, mesmo que por um usuário inativo.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setLogin(dto.login());
        // TODO: Aqui depois vamos injetar o PasswordEncoder do Spring Security para criptografar
        usuario.setSenha(dto.senha());
        usuario.setExcluido(false);

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findByIdAndExcluidoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado ou inativo."));
    }

    @Transactional
    public void removerLogico(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setExcluido(true); // Soft Delete
        usuarioRepository.save(usuario);
    }
}