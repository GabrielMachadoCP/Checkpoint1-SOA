package br.com.fiap.byteshoponlineapp.repository;

import br.com.fiap.byteshoponlineapp.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsById(Long id);
}