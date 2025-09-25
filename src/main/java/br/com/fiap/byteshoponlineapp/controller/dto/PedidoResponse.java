package br.com.fiap.byteshoponlineapp.controller.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class PedidoResponse {
    private Long id;
    private Long clienteId;
    private BigDecimal total;
    private String status;
    private List<ItemPedidoResponse> itens;
    private PagamentoResponse pagamento; // pode ser null
}