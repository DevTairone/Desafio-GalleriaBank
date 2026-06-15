package galleriabank.compras.core.application;

import galleriabank.compras.core.domain.Usuario;
import galleriabank.compras.core.ports.UsuarioRepositoryPort;
import galleriabank.compras.infrastructure.web.dtos.request.UsuarioRequestDTO;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import galleriabank.compras.application.usecases.UsuarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioUseCase usuarioUseCase;
    private Usuario usuarioAtivo;
    private UsuarioRequestDTO usuarioRequestDTO;

    @BeforeEach
    void setUp() {
        usuarioUseCase = new UsuarioUseCase(usuarioRepository, passwordEncoder);

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
    @DisplayName("Deve realizar a deleção lógica mudando o status de excluído para verdadeiro")
    void deveRealizarDelecaoLogicaComSucesso() {
        when(usuarioRepository.findByIdAndExcluidoFalse(1L)).thenReturn(Optional.of(usuarioAtivo));
        when(usuarioRepository.save(any())).thenReturn(usuarioAtivo);

        usuarioUseCase.removerLogico(1L);

        assertTrue(usuarioAtivo.isExcluido(), "O usuário deveria estar marcado como excluído (true) após a remoção lógica");
        verify(usuarioRepository, times(1)).save(usuarioAtivo);
    }
}

