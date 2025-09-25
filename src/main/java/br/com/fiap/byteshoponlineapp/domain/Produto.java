package br.com.fiap.byteshoponlineapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "produto", schema = "byteshop_db")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    private String nome;

    @NotNull
    @DecimalMin(value = "0.01")
    private java.math.BigDecimal preco;

    @Size(max = 60)
    private String categoria;

    @Lob
    private String descricao;

    @NotNull
    private Boolean ativo = Boolean.TRUE;
}
