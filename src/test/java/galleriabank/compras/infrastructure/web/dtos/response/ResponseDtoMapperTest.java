package galleriabank.compras.infrastructure.web.dtos.response;

import galleriabank.compras.core.domain.Cliente;
import galleriabank.compras.core.domain.Pedido;
import galleriabank.compras.core.domain.Produto;
import galleriabank.compras.core.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseDtoMapperTest {

    @Test
    @DisplayName("UsuarioResponseDTO.fromEntity deve mapear os campos corretamente")
    void usuarioResponseDtoFromEntity() {
        Usuario usuario = new Usuario(1L, "Tairone", "tairone.dev", "senha", false);

        UsuarioResponseDTO dto = UsuarioResponseDTO.fromEntity(usuario);

        assertEquals(1L, dto.id());
        assertEquals("Tairone", dto.nome());
        assertEquals("tairone.dev", dto.login());
    }

    @Test
    @DisplayName("ClienteResponseDTO.fromEntity deve mapear os campos corretamente")
    void clienteResponseDtoFromEntity() {
        Cliente cliente = new Cliente(2L, "Cliente", "52998224725", "11999999999");

        ClienteResponseDTO dto = ClienteResponseDTO.fromEntity(cliente);

        assertEquals(2L, dto.id());
        assertEquals("Cliente", dto.nome());
        assertEquals("52998224725", dto.cpf());
        assertEquals("11999999999", dto.telefone());
    }

    @Test
    @DisplayName("PedidoResponseDTO.fromEntity deve mapear cliente, produtos e total")
    void pedidoResponseDtoFromEntity() {
        Cliente cliente = new Cliente(3L, "Cliente Pedido", "52998224725", "11988887777");
        Produto p1 = new Produto(10L, "P1", new BigDecimal("10.00"));
        Produto p2 = new Produto(11L, "P2", new BigDecimal("5.50"));

        Pedido pedido = new Pedido();
        pedido.setId(20L);
        pedido.setNumero("PED-20");
        pedido.setDataEmissao(LocalDateTime.now());
        pedido.setDescricao("Pedido DTO");
        pedido.setCliente(cliente);
        pedido.setProdutos(List.of(p1, p2));

        PedidoResponseDTO dto = PedidoResponseDTO.fromEntity(pedido);

        assertEquals("PED-20", dto.numero());
        assertEquals("Pedido DTO", dto.descricao());
        assertEquals(3L, dto.clienteId());
        assertEquals("Cliente Pedido", dto.clienteNome());
        assertEquals(2, dto.produtos().size());
        assertEquals(new BigDecimal("15.50"), dto.total());
    }
}

