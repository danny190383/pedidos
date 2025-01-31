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
@Table(name = "impuesto_tarifa", schema = "pedidos")
public class ImpuestoTarifa implements Serializable {
    private static final long serialVersionUID = 34L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "impuesto_tarifa_seq")
    @SequenceGenerator(name = "impuesto_tarifa_seq", sequenceName = "pedidos.impuesto_tarifa_seq", allocationSize = 1)
    @Column(name = "id_impuesto_tarifa")
    private Long idImpuestoTarifa;
    @Basic(optional = false)
    @Column(name = "codigo_sri")
    private String codigoSri;
    @Column(name = "porcentaje")
    private Integer porcentaje;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "impuesto", referencedColumnName = "id_impuesto")
    @ManyToOne(fetch = FetchType.LAZY)
    private Impuesto impuesto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImpuestoTarifa that = (ImpuestoTarifa) o;
        return Objects.equals(idImpuestoTarifa, that.idImpuestoTarifa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idImpuestoTarifa);
    }
}
