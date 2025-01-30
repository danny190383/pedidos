package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido_detalle", schema = "pedidos")
public class PedidoDetalle  implements Serializable {
    private static final long serialVersionUID = 29L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_detalle_seq")
    @SequenceGenerator(name = "pedido_detalle_seq", sequenceName = "pedidos.pedido_detalle_seq", allocationSize = 1)
    @Column(name = "id_pedido_detalle")
    private Long idPedidoDetalle;
    @Column(name = "volumen")
    private BigDecimal volumen;
    @Column(name = "costo")
    private BigDecimal costo;
    @Column(name = "subtotal")
    private BigDecimal subtotal;
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    @ManyToOne(optional = false)
    private Pedido pedido;
    @JoinColumn(name = "id_tipo_combustible", referencedColumnName = "id_tipo_combustible")
    @ManyToOne(optional = false)
    private TipoCombustible tipoCombustible;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoDetalle that = (PedidoDetalle) o;
        return Objects.equals(idPedidoDetalle, that.idPedidoDetalle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedidoDetalle);
    }

}
