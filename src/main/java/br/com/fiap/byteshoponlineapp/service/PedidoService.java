package br.com.fiap.byteshoponlineapp.service;

import br.com.fiap.byteshoponlineapp.domain.*;
import br.com.fiap.byteshoponlineapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepo;
    private final CarrinhoRepository carrinhoRepo;
    private final ItemCarrinhoRepository itemCarrinhoRepo;
    private final ItemPedidoRepository itemPedidoRepo;
    private final ClienteRepository clienteRepo;

    public PedidoService(PedidoRepository pedidoRepo, CarrinhoRepository carrinhoRepo, ItemCarrinhoRepository itemCarrinhoRepo, ItemPedidoRepository itemPedidoRepo, ClienteRepository clienteRepo) {
        this.pedidoRepo = pedidoRepo;
        this.carrinhoRepo = carrinhoRepo;
        this.itemCarrinhoRepo = itemCarrinhoRepo;
        this.itemPedidoRepo = itemPedidoRepo;
        this.clienteRepo = clienteRepo;
    }

    public Pedido criarAPartirDoCarrinho(Long carrinhoId) {
        Carrinho carrinho = carrinhoRepo.findById(carrinhoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "carrinho não encontrado"));
        List<ItemCarrinho> itens = itemCarrinhoRepo.findByCarrinho(carrinho);
        if (itens.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "carrinho vazio");

        BigDecimal total = itens.stream()
            .map(i -> i.getPrecoUnitario().multiply(java.math.BigDecimal.valueOf(i.getQuantidade())))
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        Pedido pedido = Pedido.builder()
                .cliente(carrinho.getCliente())
                .total(total)
                .status("CRIADO")
                .build();
        pedido = pedidoRepo.save(pedido);

        for (ItemCarrinho ic : itens) {
            ItemPedido ip = ItemPedido.builder()
                .pedido(pedido)
                .produto(ic.getProduto())
                .quantidade(ic.getQuantidade())
                .precoUnitario(ic.getPrecoUnitario())
                .build();
            itemPedidoRepo.save(ip);
        }
        // Opcional: limpar carrinho após gerar pedido
        // itemCarrinhoRepo.deleteAll(itens);

        return pedido;
    }

    public List<Pedido> listar() { return pedidoRepo.findAll(); }
    public Pedido obter(Long id) {
        return pedidoRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "pedido não encontrado"));
    }

    public Pedido atualizarStatus(Long id, String status) {
        Pedido p = obter(id);
        p.setStatus(status);
        return pedidoRepo.save(p);
    }
}