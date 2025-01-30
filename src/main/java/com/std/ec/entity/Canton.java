package com.std.ec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "canton", schema = "pedidos")
public class Canton implements Serializable {
	private static final long serialVersionUID = 11L;
	@EmbeddedId
    protected CantonPK cantonPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Integer estado;
    @JoinColumn(name = "id_provincia", referencedColumnName = "id_provincia", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Provincia provincia;

    @Transient
    private List<Parroquia> listParroquia;
}
