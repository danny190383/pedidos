package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido_camion", schema = "pedidos")
public class PedidoCamion implements Serializable {
    private static final long serialVersionUID = 30L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_camion_seq")
    @SequenceGenerator(name = "pedido_camion_seq", sequenceName = "pedidos.pedido_camion_seq", allocationSize = 1)
    @Column(name = "id_pedido_camion")
    private Long idPedidoCamion;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @JoinColumn(name = "id_camion", referencedColumnName = "id_camion")
    @ManyToOne(optional = true)
    private Camion camion;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario usuarioRegistra;
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    @ManyToOne(optional = false)
    private Pedido pedido;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "estado")
    private Boolean estado;
    @OneToMany(mappedBy = "pedidoCamion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoCamionSello> pedidoCamionSelloLst = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoCamion that = (PedidoCamion) o;
        return Objects.equals(idPedidoCamion, that.idPedidoCamion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedidoCamion);
    }
}
