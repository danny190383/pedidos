package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "terminal_estacion_cupo", schema = "pedidos")
public class TerminalEstacionCupo  implements Serializable {
    private static final long serialVersionUID = 41L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terminal_estacion_cupo_seq")
    @SequenceGenerator(name = "terminal_estacion_cupo_seq", sequenceName = "pedidos.terminal_estacion_cupo_seq", allocationSize = 1)
    @Column(name = "id_terminal_estacion_cupo")
    private Long idTerminalEstacionCupo;
    @Column(name = "mes")
    private String mes;
    @Column(name = "cupo_mensual")
    private BigDecimal cupoMensual;
    @Column(name = "cupo_diario")
    private BigDecimal cupoDiario;
    @Column(name = "porcentaje_mercado")
    private BigDecimal porcentajeMercado;
    @Column(name = "volumen_diario")
    private BigDecimal volumenDiario;
    @Column(name = "volumen_semanal")
    private BigDecimal volumenSemanal;
    @Column(name = "volumen_mensual")
    private BigDecimal volumenMensual;
    @JoinColumn(name = "id_terminal_estacion", referencedColumnName = "id_terminal_estacion")
    @ManyToOne(optional = false)
    private TerminalEstacion terminalEstacion;
    @JoinColumn(name = "id_tipo_combustible", referencedColumnName = "id_tipo_combustible")
    @ManyToOne(optional = false)
    private TipoCombustible tipoCombustible;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario usuarioRegistra;
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminalEstacionCupo that = (TerminalEstacionCupo) o;
        return Objects.equals(idTerminalEstacionCupo, that.idTerminalEstacionCupo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTerminalEstacionCupo);
    }
}
