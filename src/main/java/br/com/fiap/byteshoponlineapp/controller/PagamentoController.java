package br.com.fiap.byteshoponlineapp.controller;

import br.com.fiap.byteshoponlineapp.domain.Pagamento;
import br.com.fiap.byteshoponlineapp.service.PagamentoService;
import br.com.fiap.byteshoponlineapp.controller.dto.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
    private final PagamentoService service;
    public PagamentoController(PagamentoService service) { this.service = service; }

    @PostMapping
    public PagamentoResponse criar(@Validated @RequestBody PagamentoRequest req) {
        Pagamento p = service.criar(req.getPedidoId(), req.getValor(), req.getMetodo());
        return PagamentoResponse.builder().id(p.getId()).pedidoId(p.getPedido().getId()).valor(p.getValor()).status(p.getStatus()).metodo(p.getMetodo()).build();
    }

    @GetMapping("/{id}")
    public PagamentoResponse obter(@PathVariable("id") Long id) {
        Pagamento p = service.obter(id);
        return PagamentoResponse.builder().id(p.getId()).pedidoId(p.getPedido().getId()).valor(p.getValor()).status(p.getStatus()).metodo(p.getMetodo()).build();
    }

    @PutMapping("/{id}/status")
    public PagamentoResponse atualizar(@PathVariable("id") Long id, @Validated @RequestBody PagamentoStatusUpdate req) {
        Pagamento p = service.atualizarStatus(id, req.getStatus());
        return PagamentoResponse.builder().id(p.getId()).pedidoId(p.getPedido().getId()).valor(p.getValor()).status(p.getStatus()).metodo(p.getMetodo()).build();
    }
}