package galleriabank.compras.infrastructure.services;

import galleriabank.compras.core.domain.Usuario;
import galleriabank.compras.infrastructure.persistence.repositories.UsuarioRepository;
import galleriabank.compras.infrastructure.web.dtos.request.UsuarioRequestDTO;
import galleriabank.compras.infrastructure.web.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioAtivo;
    private UsuarioRequestDTO usuarioRequestDTO;

    @BeforeEach
    void setUp() {
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
            usuarioService.cadastrar(usuarioRequestDTO);
        });

        assertEquals("Este login já está em uso, mesmo que por um usuário inativo.", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve realizar a deleção lógica mudando o status de excluído para verdadeiro")
    void deveRealizarDelecaoLogicaComSucesso() {
        when(usuarioRepository.findByIdAndExcluidoFalse(1L)).thenReturn(Optional.of(usuarioAtivo));
        when(usuarioRepository.save(any())).thenReturn(usuarioAtivo);

        usuarioService.removerLogico(1L);

        assertTrue(usuarioAtivo.isExcluido(), "O usuário deveria estar marcado como excluído (true) após a remoção lógica");
        verify(usuarioRepository, times(1)).save(usuarioAtivo);
    }
}