package com.std.ec.controller;

import com.std.ec.entity.*;
import com.std.ec.service.impl.IEstacionServicioService;
import com.std.ec.service.impl.IPedidoService;
import com.std.ec.service.impl.IRazonAnulacionService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("pedidoBean")
@ViewScoped
public class PedidoBean implements Serializable {
	private static final long serialVersionUID = 26L;
    @Autowired
    private IPedidoService pedidoService;
    @Autowired
    private IEstacionServicioService estacionServicioService;
    @Autowired
    private IRazonAnulacionService razonAnulacionService;
    @Inject
    private UserSessionBean userSession;

    private Pedido pedido;
    private List<EstacionServicio> estacionServicioLst;
    private List<RazonAnulacion> listRazonAnulacion;
    private Boolean nuevoRegistro;
    private Boolean anulando;
    private PedidoEstado pedidoEstadoAnulado;

    public PedidoBean() {
        pedido = new Pedido();
        estacionServicioLst = new ArrayList<>();
        pedidoEstadoAnulado = new PedidoEstado();
        pedidoEstadoAnulado.setRazonAnulacion(new RazonAnulacion());
    }

    @PostConstruct
    public void init(){
        listRazonAnulacion = razonAnulacionService.listarActivas();
        estacionServicioLst.addAll(estacionServicioService.listarActivas());
        this.nuevoPedido();
    }

    public void nuevoPedido(){
        pedido = new Pedido();
        pedido.setFechaRegistro(new Date());
        pedido.setCodigo(pedidoService.obtenerSiguienteCodigo().toString());
        pedido.setUsuarioRegistra(userSession.getUsuario());
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setEstacionServicio(new EstacionServicio());
        pedido.setTerminal(new Terminal());
        this.nuevoRegistro = true;
        this.anulando = false;
    }

    public void generarAnular(){
        pedidoEstadoAnulado = new PedidoEstado();
        pedidoEstadoAnulado.setPedido(pedido);
        pedidoEstadoAnulado.setFechaRegistro(new Date());
        pedidoEstadoAnulado.setUsuarioRegistra(userSession.getUsuario());
        pedidoEstadoAnulado.setEstadoPedido(new EstadoPedido(4L));
        pedidoEstadoAnulado.setRazonAnulacion(new RazonAnulacion());
        this.nuevoRegistro = false;
        this.anulando = true;
    }

    public void seleccionarEstacionServicio(){
        pedido.setEstacionServicio(estacionServicioLst.stream()
                .filter(e -> e.getIdEstacionServicio().equals(pedido.getEstacionServicio().getIdEstacionServicio()))
                .findFirst()
                .orElse(null));
    }

    public void seleccionarTerminal(){
        pedido.setTerminal(pedido.getEstacionServicio().getTerminalList().stream()
                .filter(e -> e.getIdTerminal().equals(pedido.getTerminal().getIdTerminal()))
                .findFirst()
                .orElse(null));
    }

    public void agregarDetalle(TipoCombustible tipoCombustible){
        PedidoDetalle pedidoDetalle = new PedidoDetalle();
        pedidoDetalle.setPedido(pedido);
        pedidoDetalle.setTipoCombustible(tipoCombustible);
        if(tipoCombustible.obtenerCostoActivo() != null){
            pedidoDetalle.setCosto(tipoCombustible.obtenerCostoActivo().getCosto());
        }else{
            pedidoDetalle.setCosto(BigDecimal.ZERO);
        }
        pedidoDetalle.setVolumen(BigDecimal.ONE);
        pedidoDetalle.setSubtotal(BigDecimal.ZERO);
        pedido.getPedidoDetalleLst().add(pedidoDetalle);
        this.onCellEditCantidad(pedidoDetalle);
    }

    public void guardar(){
        try {
            if(this.pedido.getTotal().compareTo(BigDecimal.ZERO) != 1){
                FacesUtils.addErrorMessage("El pedido debe ser mayor que 0.");
                return;
            }
            if(this.nuevoRegistro){
                PedidoEstado pedidoEstado = new PedidoEstado();
                pedidoEstado.setPedido(pedido);
                pedidoEstado.setFechaRegistro(new Date());
                pedidoEstado.setUsuarioRegistra(userSession.getUsuario());
                pedidoEstado.setEstadoPedido(new EstadoPedido(1L));
                pedido.getPedidoEstadoLst().add(pedidoEstado);
            }
            if(this.anulando){
                pedido.getPedidoEstadoLst().add(pedidoEstadoAnulado);
            }
            pedidoService.guardarPedido(this.pedido);
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void eliminar(int index){
        try {
            this.pedido.getPedidoDetalleLst().remove(index);
            this.calcularTotal();
            FacesUtils.addInfoMessage("Registro eliminado.");
        }catch (Exception e) {
            FacesUtils.addErrorMessage("Error al eliminar el registro.");
        }
    }

    public void onCellEditCantidad(PedidoDetalle event){
        event.setSubtotal((event.getCosto().multiply(event.getVolumen())).setScale(2, BigDecimal.ROUND_HALF_UP));
        this.calcularTotal();
    }

    public void calcularTotal(){
        BigDecimal total = BigDecimal.ZERO;
        for(PedidoDetalle pedidoDetalle : this.pedido.getPedidoDetalleLst()){
            total = total.add(pedidoDetalle.getSubtotal());
        }
        pedido.setTotal(total);
    }

    public void choosePedido() {
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .resizable(false)
                .draggable(false)
                .modal(true)
                .build();
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarPedido", options, null);
    }

    public void onPedidoChosen(SelectEvent event) {
        Pedido pedidoSlc = (Pedido) event.getObject();
        if(pedidoSlc != null){
            this.pedido = pedidoSlc;
            this.nuevoRegistro = false;
            this.anulando = false;
        }
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<EstacionServicio> getEstacionServicioLst() {
        return estacionServicioLst;
    }

    public void setEstacionServicioLst(List<EstacionServicio> estacionServicioLst) {
        this.estacionServicioLst = estacionServicioLst;
    }

    public List<RazonAnulacion> getListRazonAnulacion() {
        return listRazonAnulacion;
    }

    public void setListRazonAnulacion(List<RazonAnulacion> listRazonAnulacion) {
        this.listRazonAnulacion = listRazonAnulacion;
    }

    public PedidoEstado getPedidoEstadoAnulado() {
        return pedidoEstadoAnulado;
    }

    public void setPedidoEstadoAnulado(PedidoEstado pedidoEstadoAnulado) {
        this.pedidoEstadoAnulado = pedidoEstadoAnulado;
    }
}
