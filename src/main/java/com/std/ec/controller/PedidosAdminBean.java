package com.std.ec.controller;

import com.std.ec.entity.Pedido;
import com.std.ec.service.impl.IPedidoService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("pedidosAdminBean")
@ViewScoped
public class PedidosAdminBean implements Serializable {
	private static final long serialVersionUID = 41L;

	@Autowired
    private IPedidoService pedidoService;

    private LazyDataModel<Pedido> lazyModel;
    private List<Pedido> datasource;
    private Pedido pedidoSelected;

    public PedidosAdminBean() {
        this.pedidoSelected = new Pedido();
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataModel<Pedido>() {

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return (int) pedidoService.countPedidos(filterBy);
            }

            @Override
            public List<Pedido> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                datasource = pedidoService.getPedidos(first, pageSize, sortBy, filterBy);
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

    public void asignarPrioritario(int index){
        Pedido pedido = lazyModel.getWrappedData().get(index);
        List<Pedido> pedidosLst = pedidoService.obtenerPedidosPorFechaYTurnoPrioritario(pedido.getFechaRegistro().toLocalDate(), pedido.getTerminal().getIdTerminal());
        if(pedidosLst != null && !pedidosLst.isEmpty()){
            if(pedidosLst.size() < pedido.getTerminal().getTurnosPrioritarios()){
                pedido.setTurnoPrioritario(Boolean.TRUE);
                this.guardar(pedido);
            }else{
                pedido.setTurnoPrioritario(Boolean.FALSE);
                FacesUtils.addErrorMessage("Error: limite de turnos prioritarios alcanzados.");
            }
        }else {
        	 pedido.setTurnoPrioritario(Boolean.TRUE);
             this.guardar(pedido);
        }
    }

    public void guardar(Pedido pedido){
        try {
            this.pedidoService.guardarPedido(pedido);
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void chooseCheque() {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        Map<String, List<String>> params = new HashMap<>();
        params.put("pedido", Arrays.asList(pedidoSelected.getIdPedido().toString()));
        PrimeFaces.current().dialog().openDynamic("/extraDialog/cheque", options, params);
    }

    public void onChequeChosen(SelectEvent event) {
        Pedido pedidoSlc = (Pedido) event.getObject();
        if(!pedidoSlc.getPedidoChequeLst().isEmpty()){
            this.guardar(pedidoSlc);
        }
    }

    public void chooseValidarCheque() {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        Map<String, List<String>> params = new HashMap<>();
        params.put("pedido", Arrays.asList(pedidoSelected.getIdPedido().toString()));
        PrimeFaces.current().dialog().openDynamic("/extraDialog/cheque", options, params);
    }

    public void onValidarChequeChosen(SelectEvent event) {
        Pedido pedidoSlc = (Pedido) event.getObject();

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
}
