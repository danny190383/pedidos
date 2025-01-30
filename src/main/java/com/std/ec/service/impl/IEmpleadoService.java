package com.std.ec.service.impl;

import com.std.ec.entity.Empleado;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import java.util.List;
import java.util.Map;

public interface IEmpleadoService {
    List<Empleado> listar();

    Empleado buscarCedula(String cedula);

    void eliminarEmpleado(Empleado empleado);

    void guardarEmpleado(Empleado empleado);

    List<Empleado> getEmpleados(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);

    long countEmpleados(Map<String, FilterMeta> filterBy);
}
