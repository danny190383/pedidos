package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido_camion_sello", schema = "pedidos")
public class PedidoCamionSello implements Serializable {
    private static final long serialVersionUID = 32L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_camion_sello_seq")
    @SequenceGenerator(name = "pedido_camion_sello_seq", sequenceName = "pedido_camion_sello_seq", allocationSize = 1)
    @Column(name = "id_pedido_camion_sello")
    private Long idPedidoCamionSello;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario usuarioRegistra;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    @ManyToOne(optional = false)
    private PedidoCamion pedidoCamion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoCamionSello that = (PedidoCamionSello) o;
        return Objects.equals(idPedidoCamionSello, that.idPedidoCamionSello);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedidoCamionSello);
    }
}
