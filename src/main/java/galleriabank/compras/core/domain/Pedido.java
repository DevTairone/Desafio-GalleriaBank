package galleriabank.compras.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @PrePersist
    protected void onCreate() {
        this.dataEmissao = LocalDateTime.now();
    }

    public double getTotal() {
        return produtos.stream()
                .mapToDouble(p -> p.getValor().doubleValue())
                .sum();
    }
}