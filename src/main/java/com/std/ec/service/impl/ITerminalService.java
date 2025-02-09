package com.std.ec.service.impl;

import com.std.ec.entity.Terminal;
import java.util.List;

public interface ITerminalService {
    List<Terminal> listar();

    List<Terminal> findAllWithRelations();

    void eliminarTerminal(Terminal terminal);

    void guardarTerminal(Terminal terminal);
}
