package com.std.ec.service.impl;

import com.std.ec.entity.Persona;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public interface IPersonaService {
    Persona buscarCedula(String cedula);

    void guardarPersona(Persona persona);

    void eliminarPersona(Persona persona);

    List<Persona> getPersonas(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);

    long countPersonas(Map<String, FilterMeta> filterBy);
}
