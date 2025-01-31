package com.std.ec.service.impl;

import com.std.ec.entity.ImpuestoTarifa;

import java.util.List;

public interface IImpuestoTarifaService {
    List<ImpuestoTarifa> listar();

    List<ImpuestoTarifa> getTarifasActivasPorImpuesto(Long idImpuesto);
}
