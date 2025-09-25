package br.com.fiap.byteshoponlineapp.service;

import br.com.fiap.byteshoponlineapp.domain.Produto;
import br.com.fiap.byteshoponlineapp.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository repo;
    public ProdutoService(ProdutoRepository repo) { this.repo = repo; }

    public List<Produto> listar() { return repo.findAll(); }
    public Produto obter(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produto não encontrado"));
    }
    public Produto criar(Produto p) { return repo.save(p); }
    public Produto atualizar(Long id, Produto p) {
        Produto atual = obter(id);
        atual.setNome(p.getNome());
        atual.setPreco(p.getPreco());
        atual.setCategoria(p.getCategoria());
        atual.setDescricao(p.getDescricao());
        atual.setAtivo(p.getAtivo());
        return repo.save(atual);
    }
    public void excluir(Long id) {
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "produto em uso e não pode ser removido");
        }
    }
}