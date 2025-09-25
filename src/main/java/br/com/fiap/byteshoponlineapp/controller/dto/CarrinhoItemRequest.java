package br.com.fiap.byteshoponlineapp.controller.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
public class CarrinhoItemRequest {
    @NotNull private Long produtoId;
    @NotNull @Min(1) private Integer quantidade;
}