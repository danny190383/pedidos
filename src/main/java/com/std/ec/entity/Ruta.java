package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ruta", schema = "pedidos")
public class Ruta implements Serializable {
	private static final long serialVersionUID = 21L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Long idRuta;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "ruta_url")
    private String rutaUrl;
    @Column(name = "nivel")
    private Integer nivel;
    @Column(name = "orden")
    private Integer orden;
    @Column(name = "icon")
    private String icon;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "id_padre", referencedColumnName = "id_ruta")
    @ManyToOne(fetch = FetchType.LAZY)
    private Ruta padre;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ruta_rol",
            schema = "pedidos",
            joinColumns = @JoinColumn(name = "id_ruta"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Rol> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruta that = (Ruta) o;
        return Objects.equals(idRuta, that.idRuta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRuta);
    }

}
