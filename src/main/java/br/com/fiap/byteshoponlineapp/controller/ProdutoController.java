package br.com.fiap.byteshoponlineapp.controller;

import br.com.fiap.byteshoponlineapp.domain.Produto;
import br.com.fiap.byteshoponlineapp.service.ProdutoService;
import br.com.fiap.byteshoponlineapp.controller.dto.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoService service;
    public ProdutoController(ProdutoService service) { this.service = service; }

    @GetMapping
    public List<ProdutoResponseDTO> listar() {
        return service.listar().stream().map(p -> ProdutoResponseDTO.builder()
            .id(p.getId()).nome(p.getNome()).preco(p.getPreco()).categoria(p.getCategoria()).descricao(p.getDescricao()).ativo(p.getAtivo()).build()).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProdutoResponseDTO obter(@PathVariable("id") Long id) {
        var p = service.obter(id);
        return ProdutoResponseDTO.builder()
            .id(p.getId()).nome(p.getNome()).preco(p.getPreco()).categoria(p.getCategoria()).descricao(p.getDescricao()).ativo(p.getAtivo()).build();
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@Validated @RequestBody ProdutoRequestDTO dto) {
        var p = service.criar(Produto.builder().nome(dto.getNome()).preco(dto.getPreco()).categoria(dto.getCategoria()).descricao(dto.getDescricao()).ativo(dto.getAtivo()).build());
        return ResponseEntity.created(URI.create("/produtos/" + p.getId()))
            .body(ProdutoResponseDTO.builder().id(p.getId()).nome(p.getNome()).preco(p.getPreco()).categoria(p.getCategoria()).descricao(p.getDescricao()).ativo(p.getAtivo()).build());
    }

    @PutMapping("/{id}")
    public ProdutoResponseDTO atualizar(@PathVariable("id") Long id, @Validated @RequestBody ProdutoRequestDTO dto) {
        var p = service.atualizar(id, Produto.builder().nome(dto.getNome()).preco(dto.getPreco()).categoria(dto.getCategoria()).descricao(dto.getDescricao()).ativo(dto.getAtivo()).build());
        return ProdutoResponseDTO.builder().id(p.getId()).nome(p.getNome()).preco(p.getPreco()).categoria(p.getCategoria()).descricao(p.getDescricao()).ativo(p.getAtivo()).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}