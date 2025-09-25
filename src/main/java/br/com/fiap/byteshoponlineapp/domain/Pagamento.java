package br.com.fiap.byteshoponlineapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pagamento", schema = "byteshop_db", uniqueConstraints = @UniqueConstraint(columnNames = "pedido_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pagamento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_pagamento_pedido"))
    private Pedido pedido;

    @NotNull @DecimalMin("0.00")
    private BigDecimal valor;

    @NotBlank
    private String status; // PENDENTE, APROVADO, RECUSADO

    @NotBlank
    private String metodo; // PIX, CARTAO, BOLETO
}
