package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transportista", schema = "pedidos")
public class Transportista implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transportista_seq")
    @SequenceGenerator(name = "transportista_seq", sequenceName = "pedidos.transportista_seq", allocationSize = 1)
    @Column(name = "id_transportista")
    private Long idTransportista;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    @OneToOne(optional = false, cascade = CascadeType.MERGE)
    private Persona persona;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transportista that = (Transportista) o;
        return Objects.equals(idTransportista, that.idTransportista);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransportista);
    }
}
