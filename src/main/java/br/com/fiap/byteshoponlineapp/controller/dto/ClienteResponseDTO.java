package br.com.fiap.byteshoponlineapp.controller.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String documento;
}