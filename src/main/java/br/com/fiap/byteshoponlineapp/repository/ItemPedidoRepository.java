package br.com.fiap.byteshoponlineapp.repository;

import br.com.fiap.byteshoponlineapp.domain.ItemPedido;
import br.com.fiap.byteshoponlineapp.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedido(Pedido pedido);
}