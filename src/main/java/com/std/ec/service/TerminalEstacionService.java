package com.std.ec.service;

import com.std.ec.entity.TerminalEstacion;
import com.std.ec.repository.TerminalEstacionRepository;
import com.std.ec.service.impl.ITerminalEstacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalEstacionService  implements ITerminalEstacionService {

    @Autowired
    private TerminalEstacionRepository terminalEstacionRepository;

    @Override
    public List<TerminalEstacion> listarTerminalEstacionesActivasPorTerminal(Long idTerminal){
        return terminalEstacionRepository.listarTerminalEstacionesActivasPorTerminal(idTerminal);
    }

    @Override
    public TerminalEstacion guardarTerminalEstacion(TerminalEstacion terminalEstacion){
        return terminalEstacionRepository.save(terminalEstacion);
    }
}
