package com.std.ec.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ParroquiaPK implements Serializable {
	private static final long serialVersionUID = 16L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_provincia")
    private Long idProvincia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_canton")
    private Long idCanton;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_parroquia")
    private Long idParroquia;
}
