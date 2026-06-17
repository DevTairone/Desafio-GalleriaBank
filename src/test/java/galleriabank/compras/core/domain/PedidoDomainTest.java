package galleriabank.compras.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PedidoDomainTest {

    @Test
    @DisplayName("getTotal deve somar corretamente os valores dos produtos")
    void getTotalDeveSomarValores() {
        Produto p1 = new Produto(1L, "P1", new BigDecimal("10.00"));
        Produto p2 = new Produto(2L, "P2", new BigDecimal("5.50"));
        Pedido pedido = new Pedido();
        pedido.setProdutos(List.of(p1, p2));

        assertEquals(new BigDecimal("15.50"), pedido.getTotal());
    }

    @Test
    @DisplayName("getTotal com lista vazia deve retornar zero")
    void getTotalComListaVaziaRetornaZero() {
        Pedido pedido = new Pedido();
        pedido.setProdutos(List.of());

        assertEquals(BigDecimal.ZERO, pedido.getTotal());
    }

    @Test
    @DisplayName("setProdutos deve fazer copia defensiva da lista")
    void setProdutosDeveFazerCopiaDefensiva() {
        Produto produto = new Produto(1L, "P1", new BigDecimal("10.00"));
        List<Produto> externos = new ArrayList<>();
        externos.add(produto);

        Pedido pedido = new Pedido();
        pedido.setProdutos(externos);
        externos.clear();

        assertEquals(1, pedido.getProdutos().size());
    }

    @Test
    @DisplayName("onCreate deve preencher dataEmissao")
    void onCreateDevePreencherDataEmissao() {
        Pedido pedido = new Pedido();

        pedido.onCreate();

        assertNotNull(pedido.getDataEmissao());
    }
}

