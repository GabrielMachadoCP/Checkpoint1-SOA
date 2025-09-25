package br.com.fiap.byteshoponlineapp.controller.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
public class PedidoCreateRequest {
    @NotNull
    private Long carrinhoId;
}