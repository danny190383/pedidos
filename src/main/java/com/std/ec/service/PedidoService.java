package com.std.ec.service;

import com.std.ec.entity.Pedido;
import com.std.ec.repository.PedidoRepository;
import com.std.ec.service.impl.IPedidoService;
import jakarta.persistence.criteria.Expression;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PedidoService implements IPedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public List<Pedido> listar() {
        return (List<Pedido>) pedidoRepository.findAll();
    }

    @Override
    public Pedido findPedidoById(Long id) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(id);
        return pedidoOptional.orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    @Override
    public void guardarPedido(Pedido pedido){
        pedidoRepository.save(pedido);
    }

    @Override
    public Integer obtenerSiguienteCodigo() {
        Integer maxCodigo = pedidoRepository.findMaxCodigo();
        return (maxCodigo != null) ? maxCodigo + 1 : 1;
    }

    @Override
    public List<Pedido> obtenerPedidosPorFechaYTurnoPrioritario(LocalDate fecha, Long idTerminal) {
        return pedidoRepository.findPedidosByFechaAndTurnoPrioritarioAndTerminal(fecha, idTerminal);
    }

    @Override
    public long countPedidos(Map<String, FilterMeta> filterBy) {
        Specification<Pedido> specification = (root, query, criteriaBuilder) -> {
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

                        // Verificar si el campo es de tipo LocalDateTime
                        if (path.getJavaType() == LocalDateTime.class) {
                            // Convertir el valor de filtro (String) a LocalDate
                            try {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDate fechaFiltro = LocalDate.parse(value.toString(), formatter);

                                // Extraer la fecha del campo LocalDateTime en la base de datos
                                Expression<LocalDate> fechaCampo = criteriaBuilder.function(
                                        "DATE", // Función de la base de datos para extraer la fecha
                                        LocalDate.class,
                                        path
                                );

                                // Comparar solo la parte de la fecha
                                predicate = criteriaBuilder.and(predicate,
                                        criteriaBuilder.equal(fechaCampo, fechaFiltro));
                            } catch (DateTimeParseException e) {
                                throw new IllegalArgumentException("Formato de fecha inválido: " + value.toString(), e);
                            }
                        } else {
                            // Si no es una fecha, aplicar lower() como antes
                            predicate = criteriaBuilder.and(predicate,
                                    criteriaBuilder.like(
                                            criteriaBuilder.lower(path.as(String.class)),
                                            "%" + value.toString().toLowerCase() + "%"
                                    ));
                        }
                    }
                }
            }

            return predicate;
        };

        return pedidoRepository.count(specification);
    }

    @Override
    public List<Pedido> getPedidos(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
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
        Specification<Pedido> specification = (root, query, criteriaBuilder) -> {
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

                        // Verificar si el campo es de tipo LocalDateTime
                        if (path.getJavaType() == LocalDateTime.class) {
                            // Convertir el valor de filtro (String) a LocalDate
                            try {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDate fechaFiltro = LocalDate.parse(value.toString(), formatter);

                                // Extraer la fecha del campo LocalDateTime en la base de datos
                                Expression<LocalDate> fechaCampo = criteriaBuilder.function(
                                        "DATE", // Función de la base de datos para extraer la fecha
                                        LocalDate.class,
                                        path
                                );

                                // Comparar solo la parte de la fecha
                                predicate = criteriaBuilder.and(predicate,
                                        criteriaBuilder.equal(fechaCampo, fechaFiltro));
                            } catch (DateTimeParseException e) {
                                throw new IllegalArgumentException("Formato de fecha inválido: " + value.toString(), e);
                            }
                        } else {
                            // Si no es una fecha, aplicar lower() como antes
                            predicate = criteriaBuilder.and(predicate,
                                    criteriaBuilder.like(
                                            criteriaBuilder.lower(path.as(String.class)),
                                            "%" + value.toString().toLowerCase() + "%"
                                    ));
                        }
                    }
                }
            }

            return predicate;
        };

        // Obtener datos paginados
        Page<Pedido> page = pedidoRepository.findAll(specification, pageRequest);

        return page.getContent();
    }
}
