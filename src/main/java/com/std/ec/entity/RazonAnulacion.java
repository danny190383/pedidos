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
@Table(name = "razon_anulacion", schema = "pedidos")
public class RazonAnulacion implements Serializable {
	private static final long serialVersionUID = 19L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "razon_anulacion_seq")
    @SequenceGenerator(name = "razon_anulacion_seq", sequenceName = "pedidos.razon_anulacion_seq", allocationSize = 1)
    @Column(name = "id_razon_anulacion")
    private Long idRazonAnulacion;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "estado")
    private Boolean estado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RazonAnulacion that = (RazonAnulacion) o;
        return Objects.equals(idRazonAnulacion, that.idRazonAnulacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRazonAnulacion);
    }
}
