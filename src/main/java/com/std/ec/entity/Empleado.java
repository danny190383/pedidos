package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empleado", schema = "pedidos")
public class Empleado implements Serializable {
	private static final long serialVersionUID = 13L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empleado_seq")
    @SequenceGenerator(name = "empleado_seq", sequenceName = "pedidos.empleado_seq", allocationSize = 1)
    @Column(name = "id_empleado")
    private Long idEmpleado;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    @OneToOne(optional = false, cascade = CascadeType.MERGE)
    private Persona persona;
    @JoinColumn(name = "id_terminal", referencedColumnName = "id_terminal")
    @OneToOne(optional = true)
    private Terminal terminal;
    @JoinColumn(name = "id_estacion_servicio", referencedColumnName = "id_estacion_servicio")
    @OneToOne(optional = true)
    private EstacionServicio estacionServicio;
    @OneToOne(mappedBy = "empleado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Usuario usuario;

    public Empleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @Override
    public int hashCode() {
        return idEmpleado != null ? idEmpleado.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Empleado other = (Empleado) obj;
        return idEmpleado != null && idEmpleado.equals(other.idEmpleado);
    }

    @Override
    public String toString() {
        return "Empleado{idEmpleado=" + idEmpleado + '}';
    }
}
