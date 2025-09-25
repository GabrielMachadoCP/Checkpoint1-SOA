package br.com.fiap.byteshoponlineapp.controller.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
public class PagamentoRequest {
    @NotNull private Long pedidoId;
    @NotNull @DecimalMin("0.00") private BigDecimal valor;
    @NotBlank private String metodo; // PIX, CARTAO, BOLETO
}