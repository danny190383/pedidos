package com.std.ec.service;

import com.std.ec.entity.*;
import com.std.ec.repository.PedidoDetalleRepository;
import com.std.ec.repository.TerminalEstacionCupoRepository;
import com.std.ec.service.impl.IPedidoDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
public class PedidoDetalleService implements IPedidoDetalleService {

    @Autowired
    private TerminalEstacionCupoRepository terminalEstacionCupoRepository;
    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    @Override
    public void validarCupoPedido(PedidoDetalle pedidoDetalle) {
        LocalDate fechaPedido = pedidoDetalle.getPedido().getFechaRegistro().toLocalDate();
        String mesAnio = fechaPedido.getYear() + " - " + fechaPedido.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"));
        TipoCombustible tipoCombustible = pedidoDetalle.getTipoCombustible();
        EstacionServicio estacionServicio = pedidoDetalle.getPedido().getEstacionServicio();
        Terminal terminal = pedidoDetalle.getPedido().getTerminal();

        // Obtener cupo diario y mensual
        TerminalEstacionCupo cupo = terminalEstacionCupoRepository.findByTerminalAndEstacionServicioAndTipoCombustibleAndCupoMensual(terminal, estacionServicio, tipoCombustible, mesAnio);
        if (cupo == null) {
            throw new IllegalArgumentException("No hay cupo registrado para: " + mesAnio);
        }

        BigDecimal cupoDiario = cupo.getCupoDiario();
        BigDecimal cupoMensual = cupo.getCupoMensual();
        BigDecimal volumenSolicitado = pedidoDetalle.getVolumen();

        // Validar cupo diario
        if (volumenSolicitado.compareTo(cupoDiario) > 0) {
            throw new IllegalArgumentException("El volumen solicitado excede el cupo diario asignado");
        }

        BigDecimal volumenTotalMes = pedidoDetalleRepository.sumarVolumenDespachadoPorEstacionTerminalYCombustible(
                estacionServicio, terminal, tipoCombustible, fechaPedido.getYear(), fechaPedido.getMonthValue(), Pedido.DESPACHADO);

        if (volumenTotalMes.add(volumenSolicitado).compareTo(cupoMensual) > 0) {
            throw new IllegalArgumentException("El volumen solicitado excede el cupo mensual asignado");
        }
    }

    @Override
    public BigDecimal sumarVolumenDespachadoPorEstacionTerminalYCombustible(
            EstacionServicio estacionServicio,
            Terminal terminal,
            TipoCombustible tipoCombustible,
            int anio,
            int mes,
            Long estado){
        return pedidoDetalleRepository.sumarVolumenDespachadoPorEstacionTerminalYCombustible(
            estacionServicio,
            terminal,
            tipoCombustible,
            anio,
            mes,
            estado);
    }
}