package galleriabank.compras.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
// ...existing code...
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    @Column(nullable = false)
    private LocalDateTime dataEmissao;

    private String descricao;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotEmpty(message = "O pedido deve ter ao menos 1 produto")
    @ManyToMany
    @JoinTable(
            name = "tb_pedido_produtos",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<Produto> produtos = new ArrayList<>();

    public Pedido() {}

    public Pedido(Long id, String numero, LocalDateTime dataEmissao, String descricao, Cliente cliente, List<Produto> produtos) {
        this.id = id;
        this.numero = numero;
        this.dataEmissao = dataEmissao;
        this.descricao = descricao;
        this.cliente = cliente;
        this.produtos = produtos != null ? produtos : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public LocalDateTime getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDateTime dataEmissao) { this.dataEmissao = dataEmissao; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos != null ? new ArrayList<>(produtos) : new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        this.dataEmissao = LocalDateTime.now();
    }

    public BigDecimal getTotal() {
        return produtos.stream()
                .map(p -> p.getValor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}