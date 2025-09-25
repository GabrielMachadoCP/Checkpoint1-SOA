package br.com.fiap.byteshoponlineapp.repository;

import br.com.fiap.byteshoponlineapp.domain.ItemCarrinho;
import br.com.fiap.byteshoponlineapp.domain.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {
    List<ItemCarrinho> findByCarrinho(Carrinho carrinho);
}