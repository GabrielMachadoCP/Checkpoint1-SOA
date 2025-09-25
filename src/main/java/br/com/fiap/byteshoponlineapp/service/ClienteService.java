package br.com.fiap.byteshoponlineapp.service;

import br.com.fiap.byteshoponlineapp.domain.Cliente;
import br.com.fiap.byteshoponlineapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository repo;
    public ClienteService(ClienteRepository repo) { this.repo = repo; }

    public List<Cliente> listar() { return repo.findAll(); }

    public Cliente obter(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "cliente não encontrado"));
    }

    public Cliente criar(Cliente c) {
        if (repo.existsByEmail(c.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email já cadastrado");
        }
        return repo.save(c);
    }

    public Cliente atualizar(Long id, Cliente c) {
        Cliente atual = obter(id);
        if (!atual.getEmail().equalsIgnoreCase(c.getEmail()) && repo.existsByEmail(c.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email já cadastrado");
        }
        atual.setNome(c.getNome());
        atual.setEmail(c.getEmail());
        atual.setDocumento(c.getDocumento());
        return repo.save(atual);
    }

    public void excluir(Long id) {
        // Requisito: 409 se houver pedidos vinculados — regra simplificada:
        // Para manter simples, assumiremos que exclusão será bloqueada por FK no banco
        // e traduzida em 409 se ocorrer violação.
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "cliente possui vínculos e não pode ser removido");
        }
    }
}