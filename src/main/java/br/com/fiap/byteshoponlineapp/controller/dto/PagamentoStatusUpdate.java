package br.com.fiap.byteshoponlineapp.controller.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
public class PagamentoStatusUpdate {
    @NotBlank
    private String status; // PENDENTE, APROVADO, RECUSADO
}