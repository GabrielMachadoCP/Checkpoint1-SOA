package br.com.fiap.byteshoponlineapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido", schema = "byteshop_db")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ItemPedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, foreignKey = @ForeignKey(name = "fk_itempedido_pedido"))
    private Pedido pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false, foreignKey = @ForeignKey(name = "fk_itempedido_produto"))
    private Produto produto;

    @NotNull @Min(1)
    private Integer quantidade;

    @NotNull @DecimalMin("0.00")
    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;
}
