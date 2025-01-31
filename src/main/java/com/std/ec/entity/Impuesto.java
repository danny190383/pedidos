package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "impuesto", schema = "pedidos")
public class Impuesto implements Serializable {
    private static final long serialVersionUID = 33L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "impuesto_seq")
    @SequenceGenerator(name = "impuesto_seq", sequenceName = "pedidos.impuesto_seq", allocationSize = 1)
    @Column(name = "id_impuesto")
    private Long idImpuesto;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "codigo_sri")
    private String codigoSri;
    @OneToMany(mappedBy = "impuesto", fetch = FetchType.EAGER)
    private List<ImpuestoTarifa> impuestoTarifaList = new ArrayList<>();

    public List<ImpuestoTarifa> getImpuestoTarifaActivos() {
        return impuestoTarifaList.stream()
                .filter(tarifa -> tarifa.getEstado())
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Impuesto that = (Impuesto) o;
        return Objects.equals(idImpuesto, that.idImpuesto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idImpuesto);
    }
}
