package br.com.fiap.byteshoponlineapp.repository;

import br.com.fiap.byteshoponlineapp.domain.Carrinho;
import br.com.fiap.byteshoponlineapp.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Optional<Carrinho> findByCliente(Cliente cliente);
    boolean existsByCliente(Cliente cliente);
}