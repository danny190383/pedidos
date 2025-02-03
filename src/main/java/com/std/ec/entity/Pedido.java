package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido", schema = "pedidos")
public class Pedido implements Serializable {
    private static final long serialVersionUID = 25L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_seq")
    @SequenceGenerator(name = "pedido_seq", sequenceName = "pedidos.pedido_seq", allocationSize = 1)
    @Column(name = "id_pedido")
    private Long idPedido;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    @JoinColumn(name = "id_terminal", referencedColumnName = "id_terminal")
    @ManyToOne(optional = false)
    private Terminal terminal;
    @JoinColumn(name = "id_estacion_servicio", referencedColumnName = "id_estacion_servicio")
    @ManyToOne(optional = false)
    private EstacionServicio estacionServicio;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario usuarioRegistra;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "iva")
    private BigDecimal iva;
    @Column(name = "total_general")
    private BigDecimal totalGeneral;
    @Column(name = "total_volumen")
    private BigDecimal totalVolumen;
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "turno_prioritario")
    private Boolean turnoPrioritario;
    @Column(name = "turno_numero")
    private Integer turnoNumero;
    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoEstado> pedidoEstadoLst = new ArrayList<>();
    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoCamion> pedidoCamionLst = new ArrayList<>();
    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoDetalle> pedidoDetalleLst = new ArrayList<>();
    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoImpuestoTarifa> pedidoImpuestoTarifaLst = new ArrayList<>();
    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cheque> pedidoChequeLst = new ArrayList<>();

    public final static Long GENERADO = 1L;
    public final static Long CHEQUE_GENERADO = 2L;
    public final static Long CHEQUE_VALIDADO = 7L;
    public final static Long TRANSPORTE_ASIGNADO = 8L;
    public final static Long ANULADO = 4L;
    public final static Long DESPACHADO = 5L;

    public List<PedidoEstado> getPedidoEstadoOrderLst() {
        if (pedidoEstadoLst == null || pedidoEstadoLst.isEmpty()) {
            return new ArrayList<>();
        }
        return pedidoEstadoLst.stream()
                .sorted(Comparator.comparing(PedidoEstado::getFechaRegistro).reversed())
                .collect(Collectors.toList());
    }

    public Boolean estaGenerado() {
        if (pedidoEstadoLst == null || pedidoEstadoLst.isEmpty()) {
            return false;
        }
        return pedidoEstadoLst.stream()
                .anyMatch(pedidoEstado -> pedidoEstado.getEstadoPedido() != null
                        && pedidoEstado.getEstadoPedido().getIdEstadoPedido().equals(GENERADO));
    }

    public Boolean estaChequeGenerado() {
        if (pedidoEstadoLst == null || pedidoEstadoLst.isEmpty()) {
            return false;
        }
        return pedidoEstadoLst.stream()
                .anyMatch(pedidoEstado -> pedidoEstado.getEstadoPedido() != null
                        && pedidoEstado.getEstadoPedido().getIdEstadoPedido().equals(CHEQUE_GENERADO));
    }

    public Boolean estaChequeValidado() {
        if (pedidoEstadoLst == null || pedidoEstadoLst.isEmpty()) {
            return false;
        }
        return pedidoEstadoLst.stream()
                .anyMatch(pedidoEstado -> pedidoEstado.getEstadoPedido() != null
                        && pedidoEstado.getEstadoPedido().getIdEstadoPedido().equals(CHEQUE_VALIDADO));
    }

    public Boolean estaTransporteAsignado() {
        if (pedidoEstadoLst == null || pedidoEstadoLst.isEmpty()) {
            return false;
        }
        return pedidoEstadoLst.stream()
                .anyMatch(pedidoEstado -> pedidoEstado.getEstadoPedido() != null
                        && pedidoEstado.getEstadoPedido().getIdEstadoPedido().equals(TRANSPORTE_ASIGNADO));
    }

    public Boolean estaFinanciero() {
        if (pedidoEstadoLst == null || pedidoEstadoLst.isEmpty()) {
            return false;
        }
        return pedidoEstadoLst.stream()
                .anyMatch(pedidoEstado -> pedidoEstado.getEstadoPedido() != null
                        && pedidoEstado.getEstadoPedido().getIdEstadoPedido() == 3L);
    }

    public Boolean estaAnulado() {
        if (pedidoEstadoLst == null || pedidoEstadoLst.isEmpty()) {
            return false;
        }
        return pedidoEstadoLst.stream()
                .anyMatch(pedidoEstado -> pedidoEstado.getEstadoPedido() != null
                        && pedidoEstado.getEstadoPedido().getIdEstadoPedido().equals(ANULADO));
    }

    public Boolean estaDespachadoo() {
        if (pedidoEstadoLst == null || pedidoEstadoLst.isEmpty()) {
            return false;
        }
        return pedidoEstadoLst.stream()
                .anyMatch(pedidoEstado -> pedidoEstado.getEstadoPedido() != null
                        && pedidoEstado.getEstadoPedido().getIdEstadoPedido().equals(DESPACHADO));
    }

    public String obtenerEstadoPrioritario() {
        if (estaAnulado()) {
            return "Anulado";
        } else if (estaDespachadoo()) {
            return "Despachado";
        } else if (estaFinanciero()) {
            return "Financiero";
        } else if (estaTransporteAsignado()) {
            return "Transporte Asignado";
        }else if (estaChequeValidado()) {
            return "Cheque Validado";
        } else if (estaChequeGenerado()) {
            return "Cheque Generado";
        } else if (estaGenerado()) {
            return "Generado";
        } else {
            return "Sin Estado";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido that = (Pedido) o;
        return Objects.equals(idPedido, that.idPedido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedido);
    }
}
