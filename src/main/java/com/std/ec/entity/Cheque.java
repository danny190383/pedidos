package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cheque", schema = "pedidos", uniqueConstraints = {
        @UniqueConstraint(columnNames = "numero")
})
public class Cheque implements Serializable {
    private static final long serialVersionUID = 37L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cheque_seq")
    @SequenceGenerator(name = "cheque_seq", sequenceName = "pedidos.cheque_seq", allocationSize = 1)
    @Column(name = "id_cheque")
    private Long idCheque;
    @Column(name = "numero")
    private String numero;
    @Column(name = "monto")
    private BigDecimal monto;
    @Column(name = "monto_letras")
    private String montoLetras;
    @Column(name = "fecha")
    private LocalDate fecha;
    @Column(name = "beneficiario")
    private String beneficiario;
    @JoinColumn(name = "id_razon_anulacion", referencedColumnName = "id_razon_anulacion")
    @ManyToOne(optional = true)
    private RazonAnulacion razonAnulacion;
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    @ManyToOne(optional = true)
    private Persona persona;
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    @ManyToOne(optional = false)
    private Pedido pedido;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario usuarioRegistra;
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "secuencial_banco")
    private String secuencialBanco;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cheque that = (Cheque) o;
        return Objects.equals(idCheque, that.idCheque);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCheque);
    }
}
