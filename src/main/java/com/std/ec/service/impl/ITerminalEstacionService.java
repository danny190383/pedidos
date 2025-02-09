package com.std.ec.service.impl;

import com.std.ec.entity.TerminalEstacion;

import java.util.List;

public interface ITerminalEstacionService {

    List<TerminalEstacion> listarTerminalEstacionesActivasPorTerminal(Long idTerminal);

    TerminalEstacion guardarTerminalEstacion(TerminalEstacion terminalEstacion);
}
