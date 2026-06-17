package galleriabank.compras.core.application;

import galleriabank.compras.application.usecases.UsuarioUseCase;
import galleriabank.compras.core.domain.Usuario;
import galleriabank.compras.core.ports.UsuarioRepositoryPort;
import galleriabank.compras.infrastructure.web.dtos.request.UsuarioRequestDTO;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.infrastructure.web.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;


    private UsuarioUseCase usuarioUseCase;
    private Usuario usuarioAtivo;
    private UsuarioRequestDTO usuarioRequestDTO;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        usuarioUseCase = new UsuarioUseCase(usuarioRepository);

        usuarioAtivo = new Usuario();
        usuarioAtivo.setId(1L);
        usuarioAtivo.setNome("Tairone");
        usuarioAtivo.setLogin("tairone.dev");
        usuarioAtivo.setSenha("senha123");
        usuarioAtivo.setExcluido(false);

        usuarioRequestDTO = new UsuarioRequestDTO("Tairone", "tairone.dev", "senha123");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar usuário com login existente, mesmo se excluído logicamente")
    void deveLancarExcecaoQuandoLoginJaExistir() {
        when(usuarioRepository.existsByLogin("tairone.dev")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            usuarioUseCase.cadastrar(usuarioRequestDTO);
        });

        assertEquals("Este login já está em uso, mesmo que por um usuário inativo.", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve cadastrar usuario com sucesso e criptografar a senha")
    void deveCadastrarUsuarioComSucesso() {
        when(usuarioRepository.existsByLogin(usuarioRequestDTO.login())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1L);
            return usuario;
        });

        Usuario salvo = usuarioUseCase.cadastrar(usuarioRequestDTO);

        assertEquals(1L, salvo.getId());
        assertEquals(usuarioRequestDTO.nome(), salvo.getNome());
        assertEquals(usuarioRequestDTO.login(), salvo.getLogin());
        assertTrue(!salvo.isExcluido());
        assertNotEquals(usuarioRequestDTO.senha(), salvo.getSenha());
        assertTrue(bCryptPasswordEncoder.matches(usuarioRequestDTO.senha(), salvo.getSenha()));
    }

    @Test
    @DisplayName("Deve lancar excecao ao buscar usuario inexistente")
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        when(usuarioRepository.findByIdAndExcluidoFalse(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> usuarioUseCase.buscarPorId(99L));

        assertEquals("Usuário não encontrado ou inativo.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve realizar a deleção lógica mudando o status de excluído para verdadeiro")
    void deveRealizarDelecaoLogicaComSucesso() {
        when(usuarioRepository.findByIdAndExcluidoFalse(1L)).thenReturn(Optional.of(usuarioAtivo));
        when(usuarioRepository.save(any())).thenReturn(usuarioAtivo);

        usuarioUseCase.removerLogico(1L);

        assertTrue(usuarioAtivo.isExcluido(), "O usuário deveria estar marcado como excluído (true) após a remoção lógica");
        verify(usuarioRepository, times(1)).save(usuarioAtivo);
    }

    @Test
    @DisplayName("Deve atualizar usuario com sucesso e criptografar nova senha")
    void deveAtualizarUsuarioComSucesso() {
        UsuarioRequestDTO dtoAtualizacao = new UsuarioRequestDTO("Novo Nome", "novo.login", "novaSenha123");

        when(usuarioRepository.findByIdAndExcluidoFalse(1L)).thenReturn(Optional.of(usuarioAtivo));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario atualizado = usuarioUseCase.atualizar(1L, dtoAtualizacao);

        assertEquals("Novo Nome", atualizado.getNome());
        assertEquals("novo.login", atualizado.getLogin());
        assertNotEquals(dtoAtualizacao.senha(), atualizado.getSenha());
        assertTrue(bCryptPasswordEncoder.matches(dtoAtualizacao.senha(), atualizado.getSenha()));
    }

    @Test
    @DisplayName("Deve lancar excecao ao atualizar usuario inexistente")
    void deveLancarExcecaoAoAtualizarUsuarioInexistente() {
        when(usuarioRepository.findByIdAndExcluidoFalse(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> usuarioUseCase.atualizar(999L, usuarioRequestDTO));

        assertEquals("Usuário não encontrado ou inativo.", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }
}

