package com.std.ec.controller;

import com.std.ec.entity.*;
import com.std.ec.service.impl.IPedidoService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Named("historicoPedidosBean")
@ViewScoped
public class HistoricoPedidosBean implements Serializable {
	private static final long serialVersionUID = 41L;

	@Autowired
    private IPedidoService pedidoService;

    private LazyDataModel<Pedido> lazyModel;
    private List<Pedido> datasource;
    private Pedido pedidoSelected;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Long estadoSeleccionado;
    private Map<String, FilterMeta> filterBy = new HashMap<>();

    public HistoricoPedidosBean() {
        this.pedidoSelected = new Pedido();
    }

    public void seleccionarPedido(Long idPedido) {
        pedidoSelected = pedidoService.findAllWithRelations(idPedido);
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataModel<Pedido>() {

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return (int) pedidoService.countPedidosTodos(filterBy);
            }

            @Override
            public List<Pedido> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> incomingFilters) {
                if (incomingFilters != null) {
                    filterBy.putAll(incomingFilters);
                }
                datasource = pedidoService.getPedidosTodos(first, pageSize, sortBy, filterBy);
                return datasource;
            }

            @Override
            public String getRowKey(Pedido pedido) {
                if (pedido == null || pedido.getIdPedido() == null) {
                    return null;
                }
                return pedido.getIdPedido().toString();
            }

            @Override
            public Pedido getRowData(String rowKey) {
                if (rowKey == null || rowKey.isEmpty()) {
                    return null;
                }
                Long id = Long.valueOf(rowKey);
                if (datasource != null) {
                    for (Pedido pedido : datasource) {
                        if (pedido.getIdPedido().equals(id)) {
                            return pedido;
                        }
                    }
                }
                return null;
            }
        };
    }

    public void filtrarPedidos() {
        filterBy.clear();

        if (fechaDesde != null && fechaHasta != null) {
            filterBy.put("fechaRegistro",
                    FilterMeta.builder()
                            .field("fechaRegistro")
                            .filterValue(Arrays.asList(fechaDesde, fechaHasta))
                            .build()
            );
        }

        if (estadoSeleccionado != null) {
            filterBy.put("estadoPrioritario",
                    FilterMeta.builder()
                            .field("estadoPrioritario")
                            .filterValue(estadoSeleccionado)
                            .build()
            );
        }
        lazyModel.setRowCount((int) pedidoService.countPedidosTodos(filterBy));
    }

    @PreDestroy
    public void destroy() {
        datasource = null;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public LazyDataModel<Pedido> getLazyModel() {
        return lazyModel;
    }

    public Pedido getPedidoSelected() {
        return pedidoSelected;
    }

    public void setPedidoSelected(Pedido pedidoSelected) {
        this.pedidoSelected = pedidoSelected;
    }

    public Long getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(Long estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }
}
