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
@Table(name = "producto_impuesto_tarifa", schema = "pedidos")
public class ProductoImpuestoTarifa implements Serializable {
    private static final long serialVersionUID = 36L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "producto_impuesto_tarifa_seq")
    @SequenceGenerator(name = "producto_impuesto_tarifa_seq", sequenceName = "pedidos.producto_impuesto_tarifa_seq", allocationSize = 1)
    @Column(name = "id_producto_impuesto_tarifa")
    private Long idProductoImpuestoTarifa;
    @JoinColumn(name = "id_impuesto_tarifa", referencedColumnName = "id_impuesto_tarifa")
    @ManyToOne(fetch = FetchType.LAZY)
    private ImpuestoTarifa impuestoTarifa;
    @JoinColumn(name = "id_tipo_combustible", referencedColumnName = "id_tipo_combustible")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoCombustible tipoCombustible;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoImpuestoTarifa that = (ProductoImpuestoTarifa) o;
        return Objects.equals(idProductoImpuestoTarifa, that.idProductoImpuestoTarifa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProductoImpuestoTarifa);
    }
}
