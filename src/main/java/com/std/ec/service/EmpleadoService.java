package com.std.ec.service;

import com.std.ec.entity.Empleado;
import com.std.ec.repository.EmpleadoRepository;
import com.std.ec.service.impl.IEmpleadoService;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmpleadoService implements IEmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public List<Empleado> listar() {
        return (List<Empleado>) empleadoRepository.findAll();
    }

    @Override
    public Empleado buscarCedula(String cedula) {
        return empleadoRepository.buscarCedula(cedula);
    }

    @Override
    public void eliminarEmpleado(Empleado empleado){
        empleadoRepository.delete(empleado);
    }

    @Override
    public void guardarEmpleado(Empleado empleado){
        empleadoRepository.save(empleado);
    }

    @Override
    public List<Empleado> getEmpleados(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        // Configurar ordenamiento dinámico
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            for (SortMeta meta : sortBy.values()) {
                String field = meta.getField();
                Sort.Order order = meta.getOrder() == SortOrder.ASCENDING
                        ? Sort.Order.asc(field)
                        : Sort.Order.desc(field);
                sort = sort.and(Sort.by(order));
            }
        }

        // Configurar paginación
        PageRequest pageRequest = PageRequest.of(first / pageSize, pageSize, sort);

        // Construir filtros dinámicos
        Specification<Empleado> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (filterBy != null) {
                for (Map.Entry<String, FilterMeta> filter : filterBy.entrySet()) {
                    String field = filter.getKey();
                    Object value = filter.getValue().getFilterValue();

                    if (value != null) {
                        // Manejar campos anidados con "persona.campo"
                        String[] fieldParts = field.split("\\.");
                        Path<?> path = root;
                        for (String part : fieldParts) {
                            path = path.get(part);
                        }

                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(path.as(String.class)),
                                        "%" + value.toString().toLowerCase() + "%"
                                ));
                    }
                }
            }

            return predicate;
        };

        // Obtener datos paginados
        Page<Empleado> page = empleadoRepository.findAll(specification, pageRequest);

        return page.getContent();
    }

    @Override
    public long countEmpleados(Map<String, FilterMeta> filterBy) {
        Specification<Empleado> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (filterBy != null) {
                for (Map.Entry<String, FilterMeta> filter : filterBy.entrySet()) {
                    String field = filter.getKey();
                    Object value = filter.getValue().getFilterValue();

                    if (value != null) {
                        
                        String[] fieldParts = field.split("\\.");
                        Path<?> path = root;
                        for (String part : fieldParts) {
                            path = path.get(part);
                        }

                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(path.as(String.class)),
                                        "%" + value.toString().toLowerCase() + "%"
                                ));
                    }
                }
            }

            return predicate;
        };

        return empleadoRepository.count(specification);
    }

}
