package galleriabank.compras.application.usecases;

import galleriabank.compras.core.domain.Usuario;
import galleriabank.compras.core.ports.UsuarioRepositoryPort;
import galleriabank.compras.infrastructure.web.dtos.request.UsuarioRequestDTO;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.infrastructure.web.exceptions.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
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
        usuario.setSenha(passwordEncoder.encode(dto.senha())); // Continua funcionando igual
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
        usuario.setExcluido(true);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long id,UsuarioRequestDTO dto) {
        Usuario usuario = buscarPorId(id);
        usuario.setNome(dto.nome());
        usuario.setLogin(dto.login());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        return usuarioRepository.save(usuario);
    }
}
