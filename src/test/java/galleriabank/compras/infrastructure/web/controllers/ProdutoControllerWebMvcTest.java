package galleriabank.compras.infrastructure.web.controllers;

import galleriabank.compras.application.usecases.ProdutoUseCase;
import galleriabank.compras.core.domain.Produto;
import galleriabank.compras.core.ports.PedidoRepositoryPort;
import galleriabank.compras.core.ports.ProdutoRepositoryPort;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProdutoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ProdutoControllerWebMvcTest.StubConfig.class)
class ProdutoControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /produtos deve retornar 201")
    void postProdutosDeveRetornarCreated() throws Exception {
        String body = """
                {
                  "descricao": "Teclado",
                  "valor": 199.90
                }
                """;

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Teclado"));
    }

    @Test
    @DisplayName("GET /produtos/{id} deve retornar 200")
    void getProdutoPorIdDeveRetornarOk() throws Exception {
        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(199.90));
    }

    @Test
    @DisplayName("PUT /produtos/{id} deve retornar 200")
    void putProdutoDeveRetornarOk() throws Exception {
        String body = """
                {
                  "descricao": "Teclado Mecanico",
                  "valor": 299.90
                }
                """;

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Teclado Mecanico"));
    }

    @Test
    @DisplayName("DELETE /produtos/{id} deve retornar 204")
    void deleteProdutoDeveRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isNoContent());
    }

    @TestConfiguration
    static class StubConfig {
        @Bean
        ProdutoUseCase produtoUseCase() {
            return new ProdutoUseCase(new ProdutoRepositoryPort() {
                @Override
                public Produto save(Produto produto) {
                    return produto;
                }

                @Override
                public java.util.Optional<Produto> findById(Long id) {
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
                public Produto cadastrar(galleriabank.compras.infrastructure.web.dtos.request.ProdutoRequestDTO dto) {
                    return new Produto(1L, dto.descricao(), dto.valor());
                }

                @Override
                public Produto buscarPorId(Long id) {
                    return new Produto(id, "Teclado", new BigDecimal("199.90"));
                }

                @Override
                public Produto atualizar(Long id, galleriabank.compras.infrastructure.web.dtos.request.ProdutoRequestDTO dto) {
                    return new Produto(id, dto.descricao(), dto.valor());
                }

                @Override
                public void deletar(Long id) {
                    // no-op
                }
            };
        }
    }
}

