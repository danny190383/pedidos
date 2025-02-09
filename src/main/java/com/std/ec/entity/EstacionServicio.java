package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estacion_servicio", schema = "pedidos", uniqueConstraints = {
        @UniqueConstraint(columnNames = "codigo")
})
public class EstacionServicio implements Serializable {
	private static final long serialVersionUID = 14L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estacion_servicio_seq")
    @SequenceGenerator(name = "estacion_servicio_seq", sequenceName = "pedidos.estacion_servicio_seq", allocationSize = 1)
    @Column(name = "id_estacion_servicio")
    private Long idEstacionServicio;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "ruc")
    private String ruc;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripción")
    private String descripción;
    @Column(name = "email")
    private String email;
    @Column(name = "telefono")
    private String telefono;
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @OneToOne(optional = false)
    private Empleado responsable;
    @JoinColumns({
            @JoinColumn(name = "id_provincia", referencedColumnName = "id_provincia"),
            @JoinColumn(name = "id_canton", referencedColumnName = "id_canton"),
            @JoinColumn(name = "id_parroquia", referencedColumnName = "id_parroquia")})
    @ManyToOne(optional = false)
    private Parroquia parroquia;
    @Column(name = "estado")
    private Boolean estado;
    @OneToMany(mappedBy = "estacionServicio",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TerminalEstacion> terminalList = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstacionServicio that = (EstacionServicio) o;
        return Objects.equals(idEstacionServicio, that.idEstacionServicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEstacionServicio);
    }
}
