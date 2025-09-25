package br.com.fiap.byteshoponlineapp.repository;

import br.com.fiap.byteshoponlineapp.domain.Pedido;
import br.com.fiap.byteshoponlineapp.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByStatus(String status);
}