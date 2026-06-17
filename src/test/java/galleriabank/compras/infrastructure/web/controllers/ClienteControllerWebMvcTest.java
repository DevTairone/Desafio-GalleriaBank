package galleriabank.compras.infrastructure.web.controllers;

import galleriabank.compras.application.usecases.ClienteUseCase;
import galleriabank.compras.core.domain.Cliente;
import galleriabank.compras.core.ports.ClienteRepositoryPort;
import galleriabank.compras.core.ports.PedidoRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ClienteControllerWebMvcTest.StubConfig.class)
class ClienteControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /clientes deve retornar 201")
    void postClientesDeveRetornarCreated() throws Exception {
        String body = """
                {
                  "nome": "Cliente A",
                  "cpf": "52998224725",
                  "telefone": "11999999999"
                }
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Cliente A"));
    }

    @Test
    @DisplayName("GET /clientes/{id} deve retornar 200")
    void getClientePorIdDeveRetornarOk() throws Exception {
        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("52998224725"));
    }

    @Test
    @DisplayName("PUT /clientes/{id} deve retornar 200")
    void putClienteDeveRetornarOk() throws Exception {
        String body = """
                {
                  "nome": "Cliente B",
                  "cpf": "12345678909",
                  "telefone": "11988887777"
                }
                """;

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cliente B"));
    }

    @Test
    @DisplayName("DELETE /clientes/{id} deve retornar 204")
    void deleteClienteDeveRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @TestConfiguration
    static class StubConfig {
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
                    return java.util.Optional.empty();
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
                public Cliente cadastrar(galleriabank.compras.infrastructure.web.dtos.request.ClienteRequestDTO dto) {
                    return new Cliente(1L, dto.nome(), dto.cpf(), dto.telefone());
                }

                @Override
                public Cliente buscarPorId(Long id) {
                    return new Cliente(id, "Cliente A", "52998224725", "11999999999");
                }

                @Override
                public void deletar(Long id) {
                    // no-op
                }

                @Override
                public Cliente atualizar(Long id, galleriabank.compras.infrastructure.web.dtos.request.ClienteRequestDTO dto) {
                    return new Cliente(id, dto.nome(), dto.cpf(), dto.telefone());
                }
            };
        }
    }
}

