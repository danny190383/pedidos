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
@Table(name = "pedido_impuesto_tarifa", schema = "pedidos")
public class PedidoImpuestoTarifa implements Serializable {
    private static final long serialVersionUID = 35L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_impuesto_tarifa_seq")
    @SequenceGenerator(name = "pedido_impuesto_tarifa_seq", sequenceName = "pedidos.pedido_impuesto_tarifa_seq", allocationSize = 1)
    @Column(name = "id_pedido_impuesto_tarifa")
    private Long idPedidoImpuestoTarifa;
    @Column(name = "base_imponible")
    private BigDecimal baseImponible;
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "etiqueta")
    private String etiqueta;
    @JoinColumn(name = "id_impuesto_tarifa", referencedColumnName = "id_impuesto_tarifa")
    @ManyToOne(fetch = FetchType.LAZY)
    private ImpuestoTarifa impuestoTarifa;
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    @ManyToOne(fetch = FetchType.LAZY)
    private Pedido pedido;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoImpuestoTarifa that = (PedidoImpuestoTarifa) o;
        return Objects.equals(idPedidoImpuestoTarifa, that.idPedidoImpuestoTarifa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedidoImpuestoTarifa);
    }
}
