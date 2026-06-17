package galleriabank.compras.infrastructure.web.controllers;

import galleriabank.compras.application.usecases.PedidoUseCase;
import galleriabank.compras.core.ports.ClienteRepositoryPort;
import galleriabank.compras.core.ports.PedidoRepositoryPort;
import galleriabank.compras.core.ports.ProdutoRepositoryPort;
import galleriabank.compras.infrastructure.web.dtos.response.PedidoResponseDTO;
import galleriabank.compras.infrastructure.web.dtos.response.ProdutoResponseDTO;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PedidoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(PedidoControllerWebMvcTest.StubConfig.class)
class PedidoControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /pedidos deve retornar 201")
    void postPedidosDeveRetornarCreated() throws Exception {
        String body = """
                {
                  "descricao": "Pedido API",
                  "clienteId": 1,
                  "produtosIds": [10]
                }
                """;

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero").value("PED-1"))
                .andExpect(jsonPath("$.clienteId").value(1));
    }

    @Test
    @DisplayName("GET /pedidos/{id} deve retornar 200")
    void getPedidoPorIdDeveRetornarOk() throws Exception {
        mockMvc.perform(get("/pedidos/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value("PED-2"))
                .andExpect(jsonPath("$.total").value(20.00));
    }

    @TestConfiguration
    static class StubConfig {
        @Bean
        PedidoUseCase pedidoUseCase() {
            return new PedidoUseCase(new PedidoRepositoryPort() {
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
            }, new ClienteRepositoryPort() {
                @Override
                public boolean existsByCpf(String cpf) {
                    return false;
                }

                @Override
                public galleriabank.compras.core.domain.Cliente save(galleriabank.compras.core.domain.Cliente cliente) {
                    return cliente;
                }

                @Override
                public java.util.Optional<galleriabank.compras.core.domain.Cliente> findById(Long id) {
                    return java.util.Optional.empty();
                }

                @Override
                public void deleteById(Long id) {
                }

                @Override
                public boolean existsById(Long id) {
                    return true;
                }
            }, new ProdutoRepositoryPort() {
                @Override
                public galleriabank.compras.core.domain.Produto save(galleriabank.compras.core.domain.Produto produto) {
                    return produto;
                }

                @Override
                public java.util.Optional<galleriabank.compras.core.domain.Produto> findById(Long id) {
                    return java.util.Optional.empty();
                }

                @Override
                public void deleteById(Long id) {
                }

                @Override
                public boolean existsById(Long id) {
                    return true;
                }
            }) {
                @Override
                public PedidoResponseDTO criarPedido(galleriabank.compras.infrastructure.web.dtos.request.PedidoRequestDTO dto) {
                    return new PedidoResponseDTO(
                            "PED-1",
                            LocalDateTime.of(2026, 6, 17, 12, 0),
                            dto.descricao(),
                            dto.clienteId(),
                            "Cliente",
                            List.of(new ProdutoResponseDTO(10L, "Produto", new BigDecimal("10.00"))),
                            new BigDecimal("10.00")
                    );
                }

                @Override
                public PedidoResponseDTO buscarPorId(Long id) {
                    return new PedidoResponseDTO(
                            "PED-2",
                            LocalDateTime.of(2026, 6, 17, 12, 30),
                            "Pedido busca",
                            1L,
                            "Cliente",
                            List.of(new ProdutoResponseDTO(11L, "Produto X", new BigDecimal("20.00"))),
                            new BigDecimal("20.00")
                    );
                }
            };
        }
    }
}

