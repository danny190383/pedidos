package com.std.ec.service.impl;

import com.std.ec.entity.RazonAnulacion;
import java.util.List;

public interface IRazonAnulacionService {
    List<RazonAnulacion> listar();

    List<RazonAnulacion> listarActivas();

    void eliminarRazonAnulacion(RazonAnulacion razonAnulacion);

    void guardarRazonAnulacion(RazonAnulacion razonAnulacion);
}
