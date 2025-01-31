package com.std.ec.service;

import com.std.ec.entity.ImpuestoTarifa;
import com.std.ec.repository.ImpuestoTarifaRepository;
import com.std.ec.service.impl.IImpuestoTarifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpuestoTarifaService implements IImpuestoTarifaService {
    @Autowired
    private ImpuestoTarifaRepository impuestoTarifaRepository;

    @Override
    public List<ImpuestoTarifa> listar() {
        return (List<ImpuestoTarifa>) impuestoTarifaRepository.findAll();
    }

    public List<ImpuestoTarifa> getTarifasActivasPorImpuesto(Long idImpuesto) {
        return impuestoTarifaRepository.findByImpuestoIdAndEstadoActivo(idImpuesto);
    }
}
