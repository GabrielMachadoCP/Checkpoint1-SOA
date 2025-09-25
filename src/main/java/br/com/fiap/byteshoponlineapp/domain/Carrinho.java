package br.com.fiap.byteshoponlineapp.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carrinho", schema = "byteshop_db", uniqueConstraints = @UniqueConstraint(columnNames = "cliente_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Carrinho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_carrinho_cliente"))
    private Cliente cliente;
}
