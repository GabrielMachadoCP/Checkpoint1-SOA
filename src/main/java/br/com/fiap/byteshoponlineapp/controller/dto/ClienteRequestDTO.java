package br.com.fiap.byteshoponlineapp.controller.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
public class ClienteRequestDTO {
    @NotBlank @Size(max = 150)
    private String nome;

    @NotBlank @Email @Size(max = 150)
    private String email;

    @NotBlank @Size(max = 30)
    private String documento;
}