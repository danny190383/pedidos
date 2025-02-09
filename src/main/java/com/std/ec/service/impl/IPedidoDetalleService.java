package com.std.ec.service.impl;

import com.std.ec.entity.EstacionServicio;
import com.std.ec.entity.PedidoDetalle;
import com.std.ec.entity.Terminal;
import com.std.ec.entity.TipoCombustible;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface IPedidoDetalleService {
    void validarCupoPedido(PedidoDetalle pedidoDetalle);

    BigDecimal sumarVolumenDespachadoPorEstacionTerminalYCombustible(
           EstacionServicio estacionServicio,
           Terminal terminal,
           TipoCombustible tipoCombustible,
           int anio,
           int mes,
           Long estado);
}
