package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido_estado", schema = "pedidos")
public class PedidoEstado implements Serializable {
    private static final long serialVersionUID = 27L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_estado_seq")
    @SequenceGenerator(name = "pedido_estado_seq", sequenceName = "pedidos.pedido_estado_seq", allocationSize = 1)
    @Column(name = "id_pedido_estado")
    private Long idPedidoEstado;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    @ManyToOne(optional = false)
    private Pedido pedido;
    @JoinColumn(name = "id_estado_pedido", referencedColumnName = "id_estado_pedido")
    @ManyToOne(optional = true)
    private EstadoPedido estadoPedido;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario usuarioRegistra;
    @JoinColumn(name = "id_razon_anulacion", referencedColumnName = "id_razon_anulacion")
    @ManyToOne(optional = true)
    private RazonAnulacion razonAnulacion;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoEstado that = (PedidoEstado) o;
        return Objects.equals(idPedidoEstado, that.idPedidoEstado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedidoEstado);
    }
}
