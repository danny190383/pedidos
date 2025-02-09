package com.std.ec.controller;

import com.std.ec.dto.CupoPedidoDTO;
import com.std.ec.entity.*;
import com.std.ec.service.impl.IPedidoDetalleService;
import com.std.ec.service.impl.IPedidoService;
import com.std.ec.service.impl.ITerminalEstacionCupoService;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Named("pedidosAdminBean")
@ViewScoped
public class PedidosAdminBean implements Serializable {
	private static final long serialVersionUID = 41L;

	@Autowired
    private IPedidoService pedidoService;
    @Autowired
    private IPedidoDetalleService pedidoDetalleService;
    @Autowired
    private ITerminalEstacionCupoService terminalEstacionCupoService;

    private LazyDataModel<Pedido> lazyModel;
    private List<Pedido> datasource;
    private Pedido pedidoSelected;
    private List<CupoPedidoDTO> cupoPedidoDTOList;

    public PedidosAdminBean() {
        this.pedidoSelected = new Pedido();
        this.cupoPedidoDTOList = new ArrayList<>();
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

    public void onRowSelect(SelectEvent<Pedido> event) {
        pedidoSelected = pedidoService.findAllWithDetalle(event.getObject().getIdPedido());
    }

    public void chooseCheque() {
        if(pedidoSelected == null){
            FacesUtils.addWarMessage("Debe seleccionar un pedido.");
            return;
        }
        //validar cupo y si no tiene cupo enviar correo
        for(PedidoDetalle detalle : pedidoSelected.getPedidoDetalleLst()){
            pedidoDetalleService.validarCupoPedido(detalle);
        }

        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        Map<String, List<String>> params = new HashMap<>();
        params.put("pedido", Arrays.asList(pedidoSelected.getIdPedido().toString()));
        PrimeFaces.current().dialog().openDynamic("/extraDialog/cheque", options, params);
    }

    public void cargarCupos(){
        cupoPedidoDTOList.clear();
        for(PedidoDetalle pedidoDetalle : pedidoSelected.getPedidoDetalleLst()){
            LocalDate fechaPedido = pedidoDetalle.getPedido().getFechaRegistro().toLocalDate();
            String mesAnio = fechaPedido.getYear() + " - " + fechaPedido.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"));
            TipoCombustible tipoCombustible = pedidoDetalle.getTipoCombustible();
            EstacionServicio estacionServicio = pedidoDetalle.getPedido().getEstacionServicio();
            Terminal terminal = pedidoDetalle.getPedido().getTerminal();
            TerminalEstacionCupo cupo = terminalEstacionCupoService.findByTerminalAndEstacionServicioAndTipoCombustibleAndCupoMensual(
                    terminal,
                    estacionServicio,
                    tipoCombustible,
                    mesAnio);
            CupoPedidoDTO cupoPedidoDTO = new CupoPedidoDTO();
            cupoPedidoDTO.setTipoCombustible(pedidoDetalle.getTipoCombustible());
            cupoPedidoDTO.setVolumenSolicitado(pedidoDetalle.getVolumen());
            if (cupo != null) {
                cupoPedidoDTO.setCupoDiario(cupo.getCupoDiario());
                cupoPedidoDTO.setCupoMensual(cupo.getCupoMensual());
                cupoPedidoDTO.setMes(cupo.getMes());
            }else{
                cupoPedidoDTO.setCupoDiario(BigDecimal.ZERO);
                cupoPedidoDTO.setCupoMensual(BigDecimal.ZERO);
            }
            cupoPedidoDTO.setCupoMesAcumulado(pedidoDetalleService.sumarVolumenDespachadoPorEstacionTerminalYCombustible(
                    estacionServicio,
                    terminal,
                    tipoCombustible,
                    fechaPedido.getYear(),
                    fechaPedido.getMonthValue(),
                    Pedido.DESPACHADO));
            cupoPedidoDTOList.add(cupoPedidoDTO);
        }
    }

    public void onChequeChosen(SelectEvent event) {
        Pedido pedidoSlc = (Pedido) event.getObject();
        if(pedidoSlc != null){
            if(!pedidoSlc.getPedidoChequeLst().isEmpty()){
                this.guardar(pedidoSlc);
            }
        }
    }

    public void chooseValidarCheque() {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        Map<String, List<String>> params = new HashMap<>();
        params.put("pedido", Arrays.asList(pedidoSelected.getIdPedido().toString()));
        PrimeFaces.current().dialog().openDynamic("/extraDialog/validar_cheque", options, params);
    }

    public void onValidarChequeChosen(SelectEvent event) {
        Pedido pedidoSlc = (Pedido) event.getObject();
        if(pedidoSlc != null){
            this.guardar(pedidoSlc);
        }
    }

    public void chooseTransporte() {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "60%");
        options.put("contentWidth","100%");
        Map<String, List<String>> params = new HashMap<>();
        params.put("pedido", Arrays.asList(pedidoSelected.getIdPedido().toString()));
        PrimeFaces.current().dialog().openDynamic("/extraDialog/transporte", options, params);
    }

    public void onTransporteChosen(SelectEvent event) {
        Pedido pedidoSlc = (Pedido) event.getObject();
        if(pedidoSlc != null){
            this.guardar(pedidoSlc);
        }
    }

    public void chooseGuia() {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "65%");
        options.put("contentWidth","100%");
        Map<String, List<String>> params = new HashMap<>();
        params.put("pedido", Arrays.asList(pedidoSelected.getIdPedido().toString()));
        PrimeFaces.current().dialog().openDynamic("/extraDialog/guia_remision", options, params);
    }

    public void onGuiaChosen(SelectEvent event) {
        Pedido pedidoSlc = (Pedido) event.getObject();
        if(pedidoSlc != null){
            this.guardar(pedidoSlc);
        }
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

    public List<CupoPedidoDTO> getCupoPedidoDTOList() {
        return cupoPedidoDTOList;
    }

    public void setCupoPedidoDTOList(List<CupoPedidoDTO> cupoPedidoDTOList) {
        this.cupoPedidoDTOList = cupoPedidoDTOList;
    }
}
