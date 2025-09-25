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
public class CarrinhoService {
    private final CarrinhoRepository carrinhoRepo;
    private final ClienteRepository clienteRepo;
    private final ItemCarrinhoRepository itemRepo;
    private final ProdutoRepository produtoRepo;

    public CarrinhoService(CarrinhoRepository carrinhoRepo, ClienteRepository clienteRepo, ItemCarrinhoRepository itemRepo, ProdutoRepository produtoRepo) {
        this.carrinhoRepo = carrinhoRepo;
        this.clienteRepo = clienteRepo;
        this.itemRepo = itemRepo;
        this.produtoRepo = produtoRepo;
    }

    public Carrinho criarCarrinho(Long clienteId) {
        Cliente cliente = clienteRepo.findById(clienteId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "cliente não encontrado"));
        if (carrinhoRepo.existsByCliente(cliente)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "cliente já possui carrinho");
        }
        return carrinhoRepo.save(Carrinho.builder().cliente(cliente).build());
    }

    public Carrinho obter(Long id) {
        return carrinhoRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "carrinho não encontrado"));
    }

    public void remover(Long id) {
        carrinhoRepo.deleteById(id);
    }

    public ItemCarrinho adicionarItem(Long carrinhoId, Long produtoId, int qtd) {
        Carrinho carrinho = obter(carrinhoId);
        Produto produto = produtoRepo.findById(produtoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produto não encontrado"));
        if (Boolean.FALSE.equals(produto.getAtivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "produto inativo");
        }
        if (qtd < 1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "quantidade deve ser >= 1");
        ItemCarrinho item = ItemCarrinho.builder()
                .carrinho(carrinho)
                .produto(produto)
                .quantidade(qtd)
                .precoUnitario(produto.getPreco())
                .build();
        return itemRepo.save(item);
    }

    public ItemCarrinho atualizarQuantidade(Long carrinhoId, Long itemId, int qtd) {
        obter(carrinhoId); // garante que carrinho existe
        ItemCarrinho item = itemRepo.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "item não encontrado"));
        if (qtd < 1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "quantidade deve ser >= 1");
        item.setQuantidade(qtd);
        return itemRepo.save(item);
    }

    public void removerItem(Long carrinhoId, Long itemId) {
        obter(carrinhoId);
        itemRepo.deleteById(itemId);
    }

    public List<ItemCarrinho> listarItens(Long carrinhoId) {
        Carrinho c = obter(carrinhoId);
        return itemRepo.findByCarrinho(c);
    }

    public BigDecimal totalCarrinho(Long carrinhoId) {
        return listarItens(carrinhoId).stream()
            .map(i -> i.getPrecoUnitario().multiply(java.math.BigDecimal.valueOf(i.getQuantidade())))
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }
}