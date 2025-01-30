package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipo_combustible_costo", schema = "pedidos")
public class TipoCombustibleCosto implements Serializable {
    private static final long serialVersionUID = 31L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_combustible_costo_seq")
    @SequenceGenerator(name = "tipo_combustible_costo_seq", sequenceName = "pedidos.tipo_combustible_costo_seq", allocationSize = 1)
    @Column(name = "id_tipo_combustible_costo")
    private Long idTipoCombustibleCosto;
    @Column(name = "costo")
    private BigDecimal costo;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario usuarioRegistra;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "id_tipo_combustible", referencedColumnName = "id_tipo_combustible")
    @ManyToOne(optional = false)
    private TipoCombustible tipoCombustible;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoCombustibleCosto that = (TipoCombustibleCosto) o;
        return Objects.equals(idTipoCombustibleCosto, that.idTipoCombustibleCosto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTipoCombustibleCosto);
    }
}
