package br.com.fiap.byteshoponlineapp.controller;

import br.com.fiap.byteshoponlineapp.domain.*;
import br.com.fiap.byteshoponlineapp.service.CarrinhoService;
import br.com.fiap.byteshoponlineapp.controller.dto.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/carrinhos")
public class CarrinhoController {
    private final CarrinhoService service;
    public CarrinhoController(CarrinhoService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<CarrinhoResponse> criar(@Validated @RequestBody CarrinhoCreateRequest req) {
        Carrinho c = service.criarCarrinho(req.getClienteId());
        return ResponseEntity.created(URI.create("/carrinhos/" + c.getId())).body(map(c));
    }

    @GetMapping("/{id}")
    public CarrinhoResponse obter(@PathVariable("id") Long id) {
        Carrinho c = service.obter(id);
        return map(c);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable("id") Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/itens")
    public CarrinhoItemResponse addItem(@PathVariable("id") Long id, @Validated @RequestBody CarrinhoItemRequest req) {
        ItemCarrinho item = service.adicionarItem(id, req.getProdutoId(), req.getQuantidade());
        return map(item);
    }

    @PutMapping("/{id}/itens/{itemId}")
    public CarrinhoItemResponse atualizarItem(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId, @Validated @RequestBody CarrinhoItemRequest req) {
        ItemCarrinho item = service.atualizarQuantidade(id, itemId, req.getQuantidade());
        return map(item);
    }

    @DeleteMapping("/{id}/itens/{itemId}")
    public ResponseEntity<Void> removerItem(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
        service.removerItem(id, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/itens")
    public List<CarrinhoItemResponse> listarItens(@PathVariable("id") Long id) {
        return service.listarItens(id).stream().map(this::map).collect(Collectors.toList());
    }

    private CarrinhoResponse map(Carrinho c) {
        List<CarrinhoItemResponse> itens = service.listarItens(c.getId()).stream().map(this::map).collect(Collectors.toList());
        BigDecimal total = service.totalCarrinho(c.getId());
        return CarrinhoResponse.builder().id(c.getId()).clienteId(c.getCliente().getId()).total(total).itens(itens).build();
    }

    private CarrinhoItemResponse map(ItemCarrinho i) {
        return CarrinhoItemResponse.builder()
            .id(i.getId())
            .produtoId(i.getProduto().getId())
            .nomeProduto(i.getProduto().getNome())
            .quantidade(i.getQuantidade())
            .precoUnitario(i.getPrecoUnitario())
            .subtotal(i.getPrecoUnitario().multiply(java.math.BigDecimal.valueOf(i.getQuantidade())))
            .build();
    }
}