package com.std.ec.service;

import com.std.ec.entity.Persona;
import com.std.ec.repository.PersonaRepository;
import com.std.ec.service.impl.IPersonaService;
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
public class PersonaService implements IPersonaService {
    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public Persona buscarCedula(String cedula) {
        return personaRepository.buscarCedula(cedula);
    }

    @Override
    public void guardarPersona(Persona persona){
        personaRepository.save(persona);
    }

    @Override
    public void eliminarPersona(Persona persona){
        personaRepository.delete(persona);
    }

    @Override
    public List<Persona> getPersonas(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {

        // Configurar ordenamiento dinámico con valores predeterminados
        Sort sort = Sort.by(Sort.Order.asc("nombres"), Sort.Order.asc("apellidos"));
        // Configurar ordenamiento dinámico
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = Sort.unsorted();
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
        Specification<Persona> specification = (root, query, criteriaBuilder) -> {
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
        Page<Persona> page = personaRepository.findAll(specification, pageRequest);

        return page.getContent();
    }

    @Override
    public long countPersonas(Map<String, FilterMeta> filterBy) {
        Specification<Persona> specification = (root, query, criteriaBuilder) -> {
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

        return personaRepository.count(specification);
    }
}
