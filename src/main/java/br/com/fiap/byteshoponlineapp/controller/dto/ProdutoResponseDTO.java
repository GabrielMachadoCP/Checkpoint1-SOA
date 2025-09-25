package br.com.fiap.byteshoponlineapp.controller.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private String categoria;
    private String descricao;
    private Boolean ativo;
}