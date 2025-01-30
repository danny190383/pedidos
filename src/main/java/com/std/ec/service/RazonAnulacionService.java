package com.std.ec.service;

import com.std.ec.entity.RazonAnulacion;
import com.std.ec.repository.RazonAnulacionRepository;
import com.std.ec.service.impl.IRazonAnulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RazonAnulacionService implements IRazonAnulacionService {
    @Autowired
    private RazonAnulacionRepository razonAnulacionRepository;

    @Override
    public List<RazonAnulacion> listar() {
        return (List<RazonAnulacion>) razonAnulacionRepository.findAll();
    }

    @Override
    public List<RazonAnulacion> listarActivas() {
        return (List<RazonAnulacion>) razonAnulacionRepository.listarActivas();
    }

    @Override
    public void eliminarRazonAnulacion(RazonAnulacion razonAnulacion){
        razonAnulacionRepository.delete(razonAnulacion);
    }

    @Override
    public void guardarRazonAnulacion(RazonAnulacion razonAnulacion){
        razonAnulacionRepository.save(razonAnulacion);
    }
}
