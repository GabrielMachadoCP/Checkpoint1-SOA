package br.com.fiap.byteshoponlineapp.controller.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
public class ProdutoRequestDTO {
    @NotBlank @Size(max = 120)
    private String nome;

    @NotNull @DecimalMin("0.01")
    private BigDecimal preco;

    @Size(max = 60)
    private String categoria;

    private String descricao;

    private Boolean ativo = Boolean.TRUE;
}