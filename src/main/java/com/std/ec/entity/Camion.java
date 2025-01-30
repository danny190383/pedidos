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
@Table(name = "camion", schema = "pedidos", uniqueConstraints = {
        @UniqueConstraint(columnNames = "codigo")
})
public class Camion implements Serializable {
    private static final long serialVersionUID = 15L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "camion_seq")
    @SequenceGenerator(name = "camion_seq", sequenceName = "pedidos.camion_seq", allocationSize = 1)
    @Column(name = "id_camion")
    private Long idCamion;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "placa")
    private String placa;
    @Column(name = "capacidad")
    private Float capacidad;
    @Column(name = "compartimientos")
    private Integer compartimientos;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    @OneToOne(optional = false, cascade = CascadeType.MERGE)
    private Persona persona;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Camion that = (Camion) o;
        return Objects.equals(idCamion, that.idCamion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCamion);
    }
}
