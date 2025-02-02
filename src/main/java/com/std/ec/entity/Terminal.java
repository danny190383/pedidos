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
@Table(name = "terminal", schema = "pedidos", uniqueConstraints = {
        @UniqueConstraint(columnNames = "codigo")
})
public class Terminal implements Serializable {
	private static final long serialVersionUID = 22L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terminal_seq")
    @SequenceGenerator(name = "terminal_seq", sequenceName = "pedidos.terminal_seq", allocationSize = 1)
    @Column(name = "id_terminal")
    private Long idTerminal;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripción")
    private String descripción;
    @Column(name = "email")
    private String email;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "turnos_prioritarios")
    private Integer turnosPrioritarios;
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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "terminal_combustible",
            schema = "pedidos",
            joinColumns = @JoinColumn(name = "id_terminal"),
            inverseJoinColumns = @JoinColumn(name = "id_tipo_combustible")
    )
    private Set<TipoCombustible> tipoCombustibleList = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "terminal_estacion",
            schema = "pedidos",
            joinColumns = @JoinColumn(name = "id_terminal"),
            inverseJoinColumns = @JoinColumn(name = "id_estacion_servicio")
    )
    private Set<EstacionServicio> estacionServicioList = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Terminal that = (Terminal) o;
        return Objects.equals(idTerminal, that.idTerminal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTerminal);
    }
}
