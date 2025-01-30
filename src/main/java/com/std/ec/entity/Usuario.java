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
@Table(name = "usuario", schema = "pedidos", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
public class Usuario implements Serializable {
	private static final long serialVersionUID = 24L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "pedidos.usuario_seq", allocationSize = 1)
    @Column(name = "id_usuario")
    private Long idUsuario;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "estado")
    private boolean estado;
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @OneToOne(optional = false, cascade = CascadeType.MERGE)
    private Empleado empleado;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            schema = "pedidos",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Rol> roles = new HashSet<>();

    @Transient
    private String passwordTmp;

    @Override
    public int hashCode() {
        return idUsuario != null ? idUsuario.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario that = (Usuario) o;
        return Objects.equals(idUsuario, that.idUsuario);
    }

    @Override
    public String toString() {
        return "Usuario{idUsuario=" + idUsuario + '}';
    }

}
