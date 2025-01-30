package com.std.ec.service.impl;

import com.std.ec.entity.Camion;
import com.std.ec.entity.Persona;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public interface ICamionService {
    List<Camion> listar();

    void eliminarCamion(Camion camion);

    void guardarCamion(Camion camion);

    List<Camion> getCamiones(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);

    long countCamiones(Map<String, FilterMeta> filterBy);
}
