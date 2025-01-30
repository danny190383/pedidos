package com.std.ec.service.impl;

import com.std.ec.entity.EstacionServicio;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public interface IEstacionServicioService {
    List<EstacionServicio> listar();

    List<EstacionServicio> listarActivas();

    void eliminarEstacionServicio(EstacionServicio terminal);

    void guardarEstacionServicio(EstacionServicio terminal);

    List<EstacionServicio> getEstacionServicio(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);

    long countEstacionServicio(Map<String, FilterMeta> filterBy);
}
