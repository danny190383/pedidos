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
@Table(name = "persona", schema = "pedidos", uniqueConstraints = {
        @UniqueConstraint(columnNames = "cedula")
})
public class Persona implements Serializable {
	private static final long serialVersionUID = 17L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persona_seq")
    @SequenceGenerator(name = "persona_seq", sequenceName = "pedidos.persona_seq", allocationSize = 1)
    @Column(name = "id_persona")
    private Long idPersona;
    @Column(name = "cedula")
    private String cedula;
    @Basic(optional = false)
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "email")
    private String email;
    @Column(name = "telefono")
    private String telefono;

    public String getNombreApellido() {
        return nombres.split("\\s+")[0] + " " + apellidos.split("\\s+")[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona that = (Persona) o;
        return Objects.equals(idPersona, that.idPersona);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPersona);
    }
}
