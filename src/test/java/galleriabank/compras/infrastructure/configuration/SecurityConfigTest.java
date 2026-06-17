package galleriabank.compras.infrastructure.configuration;

import galleriabank.compras.application.usecases.ClienteUseCase;
import galleriabank.compras.application.usecases.UsuarioUseCase;
import galleriabank.compras.core.domain.Cliente;
import galleriabank.compras.core.domain.Usuario;
import galleriabank.compras.core.ports.ClienteRepositoryPort;
import galleriabank.compras.core.ports.PedidoRepositoryPort;
import galleriabank.compras.core.ports.UsuarioRepositoryPort;
import galleriabank.compras.infrastructure.web.controllers.ClienteController;
import galleriabank.compras.infrastructure.web.controllers.UsuarioController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UsuarioController.class, ClienteController.class})
@Import({SecurityConfig.class, SecurityConfigTest.StubConfig.class})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /usuarios deve ser publico")
    void postUsuariosDeveSerPublico() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(APPLICATION_JSON)
                        .content("{" +
                                "\"nome\":\"Tairone\"," +
                                "\"login\":\"tairone.dev\"," +
                                "\"senha\":\"123456\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /clientes/{id} sem autenticacao deve retornar 401")
    void getClientesSemAutenticacaoDeveRetornar401() throws Exception {
        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("GET /clientes/{id} com autenticacao deve retornar 200")
    void getClientesComAutenticacaoDeveRetornar200() throws Exception {
        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk());
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
            };
        }

        @Bean
        ClienteUseCase clienteUseCase() {
            return new ClienteUseCase(new ClienteRepositoryPort() {
                @Override
                public boolean existsByCpf(String cpf) {
                    return false;
                }

                @Override
                public Cliente save(Cliente cliente) {
                    return cliente;
                }

                @Override
                public java.util.Optional<Cliente> findById(Long id) {
                    return java.util.Optional.of(new Cliente(id, "Cliente", "52998224725", "11999999999"));
                }

                @Override
                public void deleteById(Long id) {
                }

                @Override
                public boolean existsById(Long id) {
                    return true;
                }
            }, new PedidoRepositoryPort() {
                @Override
                public galleriabank.compras.core.domain.Pedido save(galleriabank.compras.core.domain.Pedido pedido) {
                    return pedido;
                }

                @Override
                public java.util.Optional<galleriabank.compras.core.domain.Pedido> findById(Long id) {
                    return java.util.Optional.empty();
                }

                @Override
                public boolean existsByClienteId(Long clienteId) {
                    return false;
                }

                @Override
                public boolean existsByProdutosId(Long produtoId) {
                    return false;
                }
            }) {
                @Override
                public Cliente buscarPorId(Long id) {
                    return new Cliente(id, "Cliente", "52998224725", "11999999999");
                }
            };
        }
    }
}

