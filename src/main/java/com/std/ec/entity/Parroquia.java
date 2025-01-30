package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parroquia", schema = "pedidos")
public class Parroquia implements Serializable {
	private static final long serialVersionUID = 15L;
    @EmbeddedId
    protected ParroquiaPK parroquiaPK;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Integer estado;
    @JoinColumns({
            @JoinColumn(name = "id_canton", referencedColumnName = "id_canton", insertable = false, updatable = false)
            , @JoinColumn(name = "id_provincia", referencedColumnName = "id_provincia", insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Canton canton;
}
