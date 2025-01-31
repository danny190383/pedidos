package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipo_combustible", schema = "pedidos")
public class TipoCombustible implements Serializable {
	private static final long serialVersionUID = 23L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_combustible_seq")
    @SequenceGenerator(name = "tipo_combustible_seq", sequenceName = "pedidos.tipo_combustible_seq", allocationSize = 1)
    @Column(name = "id_tipo_combustible")
    private Long idTipoCombustible;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "estado")
    private Boolean estado;
    @OneToMany(mappedBy = "tipoCombustible", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TipoCombustibleCosto> tipoCombustibleCostoLst = new ArrayList<>();
    @OneToMany(mappedBy = "tipoCombustible", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoImpuestoTarifa> productoImpuestoTarifaLst = new ArrayList<>();

    public TipoCombustible(Long idTipoCombustible, String tipo, String nombre, Boolean estado) {
        this.idTipoCombustible = idTipoCombustible;
        this.tipo = tipo;
        this.nombre = nombre;
        this.estado = estado;
    }

    public List<TipoCombustibleCosto> getTipoCombustibleCostoOrderLst() {
        if (tipoCombustibleCostoLst == null || tipoCombustibleCostoLst.isEmpty()) {
            return new ArrayList<>();
        }
        return tipoCombustibleCostoLst.stream()
                .sorted(Comparator.comparing(TipoCombustibleCosto::getFechaRegistro).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    public TipoCombustibleCosto obtenerCostoActivo() {
        if (tipoCombustibleCostoLst == null || tipoCombustibleCostoLst.isEmpty()) {
            return null;
        }
        return getTipoCombustibleCostoLst()
                .stream()
                .filter(TipoCombustibleCosto::getEstado)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoCombustible that = (TipoCombustible) o;
        return Objects.equals(idTipoCombustible, that.idTipoCombustible);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTipoCombustible);
    }
}
