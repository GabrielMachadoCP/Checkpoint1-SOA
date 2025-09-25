package br.com.fiap.byteshoponlineapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "item_carrinho", schema = "byteshop_db")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ItemCarrinho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "carrinho_id", nullable = false, foreignKey = @ForeignKey(name = "fk_itemcarrinho_carrinho"))
    private Carrinho carrinho;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false, foreignKey = @ForeignKey(name = "fk_itemcarrinho_produto"))
    private Produto produto;

    @NotNull
    @Min(1)
    private Integer quantidade;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "preco_unitario")
    private java.math.BigDecimal precoUnitario;
}
