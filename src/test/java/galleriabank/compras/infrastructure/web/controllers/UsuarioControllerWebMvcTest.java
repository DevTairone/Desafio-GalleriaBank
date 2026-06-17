package galleriabank.compras.infrastructure.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import galleriabank.compras.application.usecases.UsuarioUseCase;
import galleriabank.compras.core.domain.Usuario;
import galleriabank.compras.core.ports.UsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UsuarioControllerWebMvcTest.StubConfig.class)
class UsuarioControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /usuarios deve retornar 201 e corpo mapeado")
    void postUsuariosDeveRetornarCreated() throws Exception {
        String body = objectMapper.writeValueAsString(java.util.Map.of(
                "nome", "Tairone",
                "login", "tairone.dev",
                "senha", "123456"
        ));

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Tairone"))
                .andExpect(jsonPath("$.login").value("tairone.dev"));
    }

    @Test
    @DisplayName("GET /usuarios/{id} deve retornar 200")
    void getUsuarioPorIdDeveRetornarOk() throws Exception {
        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} deve retornar 200")
    void putUsuarioDeveRetornarOk() throws Exception {
        String body = objectMapper.writeValueAsString(java.util.Map.of(
                "nome", "Novo Nome",
                "login", "novo.login",
                "senha", "123456"
        ));

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Novo Nome"));
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} deve retornar 204")
    void deleteUsuarioDeveRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @TestConfiguration
    static class StubConfig {
        @Bean
        UsuarioUseCase usuarioUseCase() {
            return new UsuarioUseCase(new UsuarioRepositoryPort() {
                @Override
                public java.util.Optional<Usuario> findByIdAndExcluidoFalse(Long id) {
                    return java.util.Optional.empty();
                }

                @Override
                public boolean existsByLogin(String login) {
                    return false;
                }

                @Override
                public Usuario save(Usuario usuario) {
                    return usuario;
                }
            }) {
                @Override
                public Usuario cadastrar(galleriabank.compras.infrastructure.web.dtos.request.UsuarioRequestDTO dto) {
                    return new Usuario(1L, dto.nome(), dto.login(), "hash", false);
                }

                @Override
                public Usuario buscarPorId(Long id) {
                    return new Usuario(id, "Tairone", "tairone.dev", "hash", false);
                }

                @Override
                public void removerLogico(Long id) {
                    // no-op
                }

                @Override
                public Usuario atualizar(Long id, galleriabank.compras.infrastructure.web.dtos.request.UsuarioRequestDTO dto) {
                    return new Usuario(id, dto.nome(), dto.login(), "hash", false);
                }
            };
        }
    }
}

