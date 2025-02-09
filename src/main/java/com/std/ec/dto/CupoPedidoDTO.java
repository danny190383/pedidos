package com.std.ec.dto;

import com.std.ec.entity.TipoCombustible;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CupoPedidoDTO {
    private TipoCombustible tipoCombustible;
    private String mes;
    private BigDecimal volumenSolicitado;
    private BigDecimal cupoDiario;
    private BigDecimal cupoMensual;
    private BigDecimal cupoMesAcumulado;
}
