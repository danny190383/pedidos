package com.std.ec.service;

import com.std.ec.entity.Impuesto;
import com.std.ec.repository.ImpuestoRepository;
import com.std.ec.service.impl.IImpuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpuestoService implements IImpuestoService {
    @Autowired
    private ImpuestoRepository impuestoRepository;

    @Override
    public List<Impuesto> listar() {
        return (List<Impuesto>) impuestoRepository.findAll();
    }
}
