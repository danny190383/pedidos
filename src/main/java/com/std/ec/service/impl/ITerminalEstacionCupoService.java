package com.std.ec.service.impl;

import com.std.ec.entity.EstacionServicio;
import com.std.ec.entity.Terminal;
import com.std.ec.entity.TerminalEstacionCupo;
import com.std.ec.entity.TipoCombustible;

public interface ITerminalEstacionCupoService {
    TerminalEstacionCupo findByTerminalAndEstacionServicioAndTipoCombustibleAndCupoMensual(Terminal terminal,
                                                                                           EstacionServicio estacionServicio,
                                                                                           TipoCombustible tipoCombustible,
                                                                                           String mesAnio);
}
