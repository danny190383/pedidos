package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guia_remision", schema = "pedidos")
public class GuiaRemision implements Serializable {
    private static final long serialVersionUID = 38L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guia_remision_seq")
    @SequenceGenerator(name = "guia_remision_seq", sequenceName = "pedidos.guia_remision_seq", allocationSize = 1)
    @Column(name = "id_guia_remision")
    private Long idGuiaRemision;
    @Column(name = "punto_partida")
    private String puntoPartida;
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    @Column(name = "factura_numero")
    private String facturaNumero;
    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;
    @Column(name = "orden_pedido")
    private String ordenPedido;
    @Column(name = "motivo_traslado")
    private String motivoTraslado;
    @Column(name = "destino")
    private String destino;
    @Column(name = "codigo_control")
    private String codigoControl;
    @Column(name = "sellos")
    private Integer sellos;
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    @ManyToOne(optional = true)
    private Persona comercializadora;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario usuarioRegistra;
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    @ManyToOne(optional = false)
    private PedidoCamion pedidoCamion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuiaRemision that = (GuiaRemision) o;
        return Objects.equals(idGuiaRemision, that.idGuiaRemision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idGuiaRemision);
    }
}
