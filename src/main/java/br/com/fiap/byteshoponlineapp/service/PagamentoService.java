package br.com.fiap.byteshoponlineapp.service;

import br.com.fiap.byteshoponlineapp.domain.*;
import br.com.fiap.byteshoponlineapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;

@Service
public class PagamentoService {
    private final PagamentoRepository pagamentoRepo;
    private final PedidoRepository pedidoRepo;

    public PagamentoService(PagamentoRepository pagamentoRepo, PedidoRepository pedidoRepo) {
        this.pagamentoRepo = pagamentoRepo;
        this.pedidoRepo = pedidoRepo;
    }

    public Pagamento criar(Long pedidoId, java.math.BigDecimal valor, String metodo) {
        Pedido pedido = pedidoRepo.findById(pedidoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "pedido não encontrado"));
        if (pagamentoRepo.existsByPedido(pedido)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "pedido já possui pagamento");
        }
        if (valor.compareTo(pedido.getTotal()) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "valor deve ser igual ao total do pedido");
        }
        if (!(metodo.equalsIgnoreCase("PIX") || metodo.equalsIgnoreCase("CARTAO") || metodo.equalsIgnoreCase("BOLETO"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "método de pagamento inválido");
        }
        Pagamento pg = Pagamento.builder().pedido(pedido).valor(valor).status("PENDENTE").metodo(metodo.toUpperCase()).build();
        return pagamentoRepo.save(pg);
    }

    public Pagamento obter(Long id) {
        return pagamentoRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "pagamento não encontrado"));
    }

    public Pagamento atualizarStatus(Long id, String status) {
        Pagamento p = obter(id);
        if (!(status.equalsIgnoreCase("PENDENTE") || status.equalsIgnoreCase("APROVADO") || status.equalsIgnoreCase("RECUSADO"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status inválido");
        }
        p.setStatus(status.toUpperCase());
        return pagamentoRepo.save(p);
    }
}