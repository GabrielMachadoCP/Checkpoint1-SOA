package br.com.fiap.byteshoponlineapp.controller.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class PagamentoResponse {
    private Long id;
    private Long pedidoId;
    private BigDecimal valor;
    private String status;
    private String metodo;
}