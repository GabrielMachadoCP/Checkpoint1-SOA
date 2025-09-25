package br.com.fiap.byteshoponlineapp.controller;

import br.com.fiap.byteshoponlineapp.domain.Cliente;
import br.com.fiap.byteshoponlineapp.service.ClienteService;
import br.com.fiap.byteshoponlineapp.controller.dto.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService service;
    public ClienteController(ClienteService service) { this.service = service; }

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return service.listar().stream().map(c -> new ClienteResponseDTO(c.getId(), c.getNome(), c.getEmail(), c.getDocumento())).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO obter(@PathVariable("id") Long id) {
        Cliente c = service.obter(id);
        return new ClienteResponseDTO(c.getId(), c.getNome(), c.getEmail(), c.getDocumento());
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(@Validated @RequestBody ClienteRequestDTO dto) {
        Cliente c = service.criar(Cliente.builder().nome(dto.getNome()).email(dto.getEmail()).documento(dto.getDocumento()).build());
        return ResponseEntity.created(URI.create("/clientes/" + c.getId()))
                .body(new ClienteResponseDTO(c.getId(), c.getNome(), c.getEmail(), c.getDocumento()));
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO atualizar(@PathVariable("id") Long id, @Validated @RequestBody ClienteRequestDTO dto) {
        Cliente c = service.atualizar(id, Cliente.builder().nome(dto.getNome()).email(dto.getEmail()).documento(dto.getDocumento()).build());
        return new ClienteResponseDTO(c.getId(), c.getNome(), c.getEmail(), c.getDocumento());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}