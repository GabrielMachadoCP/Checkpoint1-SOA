package br.com.fiap.byteshoponlineapp.controller;

import br.com.fiap.byteshoponlineapp.domain.*;
import br.com.fiap.byteshoponlineapp.service.PedidoService;
import br.com.fiap.byteshoponlineapp.repository.ItemPedidoRepository;
import br.com.fiap.byteshoponlineapp.controller.dto.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService service;
    private final ItemPedidoRepository itemRepo;
    public PedidoController(PedidoService service, ItemPedidoRepository itemRepo) {
        this.service = service;
        this.itemRepo = itemRepo;
    }

    @PostMapping
    public PedidoResponse criar(@Validated @RequestBody PedidoCreateRequest req) {
        Pedido p = service.criarAPartirDoCarrinho(req.getCarrinhoId());
        return map(p);
    }

    @GetMapping
    public List<PedidoResponse> listar() {
        return service.listar().stream().map(this::map).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PedidoResponse obter(@PathVariable("id") Long id) {
        return map(service.obter(id));
    }

    @PutMapping("/{id}/status")
    public PedidoResponse atualizarStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        return map(service.atualizarStatus(id, status));
    }

    private PedidoResponse map(Pedido p) {
        var itens = itemRepo.findByPedido(p).stream().map(ip -> ItemPedidoResponse.builder()
            .id(ip.getId()).produtoId(ip.getProduto().getId()).nomeProduto(ip.getProduto().getNome())
            .quantidade(ip.getQuantidade()).precoUnitario(ip.getPrecoUnitario())
            .subtotal(ip.getPrecoUnitario().multiply(java.math.BigDecimal.valueOf(ip.getQuantidade())))
            .build()).collect(Collectors.toList());
        return PedidoResponse.builder().id(p.getId()).clienteId(p.getCliente().getId()).total(p.getTotal()).status(p.getStatus()).itens(itens).build();
    }
}