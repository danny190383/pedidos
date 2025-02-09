package com.std.ec.service.impl;

import com.std.ec.entity.Transportista;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public interface ITransportistaService {

    List<Transportista> listarActivas();

    void eliminarTransportista(Transportista transportista);

    void guardarTransportista(Transportista transportista);

    List<Transportista> getTransportistas(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);

    long countTransportistas(Map<String, FilterMeta> filterBy);
}
