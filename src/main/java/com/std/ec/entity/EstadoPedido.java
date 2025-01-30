package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estado_pedido", schema = "pedidos")
public class EstadoPedido implements Serializable {
    private static final long serialVersionUID = 28L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_pedido_seq")
    @SequenceGenerator(name = "estado_pedido_seq", sequenceName = "pedidos.estado_pedido_seq", allocationSize = 1)
    @Column(name = "id_estado_pedido")
    private Long idEstadoPedido;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "estado")
    private Boolean estado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadoPedido that = (EstadoPedido) o;
        return Objects.equals(idEstadoPedido, that.idEstadoPedido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEstadoPedido);
    }
}
