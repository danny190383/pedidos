package com.std.ec.service.impl;

import com.std.ec.entity.Pedido;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IPedidoService {

    List<Pedido> listar();

    Pedido findAllWithRelations(Long id);

    Pedido findAllWithDetalle(Long id);

    Integer obtenerSiguienteCodigo();

    List<Pedido> obtenerPedidosPorFechaYTurnoPrioritario(LocalDate fecha, Long IdTerminal);

    void guardarPedido(Pedido pedido);

    List<Pedido> getPedidos(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);

    long countPedidos(Map<String, FilterMeta> filterBy);

    List<Pedido> getPedidosTodos(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);

    long countPedidosTodos(Map<String, FilterMeta> filterBy);
}
