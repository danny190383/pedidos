package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "terminal_estacion", schema = "pedidos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_terminal", "id_estacion_servicio"}))
public class TerminalEstacion implements Serializable {
    private static final long serialVersionUID = 40L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terminal_estacion_seq")
    @SequenceGenerator(name = "terminal_estacion_seq", sequenceName = "pedidos.terminal_estacion_seq", allocationSize = 1)
    @Column(name = "id_terminal_estacion")
    private Long idTerminalEstacion;
    @JoinColumn(name = "id_terminal", referencedColumnName = "id_terminal")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Terminal terminal;
    @JoinColumn(name = "id_estacion_servicio", referencedColumnName = "id_estacion_servicio")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private EstacionServicio estacionServicio;
    @OneToMany(mappedBy = "terminalEstacion", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TerminalEstacionCupo> terminalEstacionCupoLst = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminalEstacion that = (TerminalEstacion) o;
        return Objects.equals(idTerminalEstacion, that.idTerminalEstacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTerminalEstacion);
    }
}
