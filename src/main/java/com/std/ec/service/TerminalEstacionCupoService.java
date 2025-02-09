package com.std.ec.service;

import com.std.ec.entity.EstacionServicio;
import com.std.ec.entity.Terminal;
import com.std.ec.entity.TerminalEstacionCupo;
import com.std.ec.entity.TipoCombustible;
import com.std.ec.repository.TerminalEstacionCupoRepository;
import com.std.ec.service.impl.ITerminalEstacionCupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalEstacionCupoService implements ITerminalEstacionCupoService {

    @Autowired
    private TerminalEstacionCupoRepository terminalEstacionCupoRepository;

    @Override
    public TerminalEstacionCupo findByTerminalAndEstacionServicioAndTipoCombustibleAndCupoMensual(Terminal terminal,
                                                                                           EstacionServicio estacionServicio,
                                                                                           TipoCombustible tipoCombustible,
                                                                                           String mesAnio){
        return terminalEstacionCupoRepository.findByTerminalAndEstacionServicioAndTipoCombustibleAndCupoMensual(terminal, estacionServicio, tipoCombustible, mesAnio);
    }
}
