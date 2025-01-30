package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
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
    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoEstado> pedidoEstadoLst = new ArrayList<>();
    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoCamion> pedidoCamionLst = new ArrayList<>();
    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoDetalle> pedidoDetalleLst = new ArrayList<>();

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
