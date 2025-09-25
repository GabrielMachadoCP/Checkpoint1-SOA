package br.com.fiap.byteshoponlineapp.controller.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class CarrinhoResponse {
    private Long id;
    private Long clienteId;
    private BigDecimal total;
    private List<CarrinhoItemResponse> itens;
}