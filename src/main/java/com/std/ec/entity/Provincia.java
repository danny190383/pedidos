package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "provincia", schema = "pedidos")
public class Provincia implements Serializable{
	private static final long serialVersionUID = 18L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_provincia")
    private Long idProvincia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Integer estado;

    @Transient
    private List<Canton> listCanton;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Provincia that = (Provincia) o;
        return Objects.equals(idProvincia, that.idProvincia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProvincia);
    }
}
