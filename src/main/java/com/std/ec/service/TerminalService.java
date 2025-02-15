package com.std.ec.service;

import com.std.ec.entity.Terminal;
import com.std.ec.repository.TerminalRepository;
import com.std.ec.service.impl.ITerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TerminalService implements ITerminalService {
    @Autowired
    private TerminalRepository terminalRepository;

    @Override
    public List<Terminal> listar() {
        return terminalRepository.findAll();
    }

    @Override
    public List<Terminal> findAllWithRelations() {
        return terminalRepository.findAllWithRelations();
    }

    @Override
    public void eliminarTerminal(Terminal terminal){
        terminalRepository.delete(terminal);
    }

    @Override
    public void guardarTerminal(Terminal terminal){
        terminalRepository.save(terminal);
    }
}
