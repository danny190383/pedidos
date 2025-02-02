package com.std.ec.controller;

import com.std.ec.entity.*;
import com.std.ec.service.impl.IEstacionServicioService;
import com.std.ec.service.impl.IImpuestoTarifaService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    @Autowired
    private IImpuestoTarifaService impuestoTarifaService;
    @Inject
    private UserSessionBean userSession;

    private Pedido pedido;
    private List<EstacionServicio> estacionServicioLst;
    private List<RazonAnulacion> listRazonAnulacion;
    private Boolean nuevoRegistro;
    private Boolean anulando;
    private PedidoEstado pedidoEstadoAnulado;
    private List<ImpuestoTarifa> listaTarifasIvaActivas;

    public PedidoBean() {
        pedido = new Pedido();
        estacionServicioLst = new ArrayList<>();
        pedidoEstadoAnulado = new PedidoEstado();
        listaTarifasIvaActivas = new ArrayList<>();
        pedidoEstadoAnulado.setRazonAnulacion(new RazonAnulacion());
    }

    @PostConstruct
    public void init(){
        listRazonAnulacion = razonAnulacionService.listarActivasPorTipo("1");
        estacionServicioLst.addAll(estacionServicioService.listarActivas());
        this.listaTarifasIvaActivas = impuestoTarifaService.getTarifasActivasPorImpuesto(1L);
        this.nuevoPedido();
    }

    public void nuevoPedido(){
        pedido = new Pedido();
        pedido.setFechaRegistro(LocalDateTime.now());
        pedido.setCodigo(pedidoService.obtenerSiguienteCodigo().toString());
        pedido.setUsuarioRegistra(userSession.getUsuario());
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setIva(BigDecimal.ZERO);
        pedido.setTotalGeneral(BigDecimal.ZERO);
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
        if(tipoCombustible.getProductoImpuestoTarifaLst() == null || tipoCombustible.getProductoImpuestoTarifaLst().isEmpty()){
            FacesUtils.addErrorMessage("Error el producto no tiene definido % de iva.");
            return;
        }
        boolean yaExiste = pedido.getPedidoDetalleLst() != null &&
                !pedido.getPedidoDetalleLst().isEmpty() &&
                pedido.getPedidoDetalleLst().stream()
                        .anyMatch(detalle -> detalle.getTipoCombustible().equals(tipoCombustible));
        if (yaExiste) {
            FacesUtils.addErrorMessage("Error: El tipo de combustible ya está agregado en el pedido.");
            return;
        }
        PedidoDetalle pedidoDetalle = new PedidoDetalle();
        pedidoDetalle.setPedido(pedido);
        pedidoDetalle.setTipoCombustible(tipoCombustible);
        pedidoDetalle.setImpuestoTarifa((tipoCombustible.getProductoImpuestoTarifaLst().get(0)).getImpuestoTarifa());
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
            this.calcularSubTotalales();
            this.calcularTotal();
            FacesUtils.addInfoMessage("Registro eliminado.");
        }catch (Exception e) {
            FacesUtils.addErrorMessage("Error al eliminar el registro.");
        }
    }

    public void onCellEditCantidad(PedidoDetalle event){
        event.setSubtotal((event.getCosto().multiply(event.getVolumen())).setScale(2, BigDecimal.ROUND_HALF_UP));
        this.calcularSubTotalales();
        this.calcularTotal();
    }

    public void calcularSubTotalales() {
        pedido.setPedidoImpuestoTarifaLst(new ArrayList<>());
        for (ImpuestoTarifa tarifa : this.listaTarifasIvaActivas){
            PedidoImpuestoTarifa impuesto = new PedidoImpuestoTarifa();
            BigDecimal subtotal = BigDecimal.ZERO;
            for (PedidoDetalle detalle : pedido.getPedidoDetalleLst()) {
                if(Objects.equals(tarifa.getIdImpuestoTarifa(), detalle.getImpuestoTarifa().getIdImpuestoTarifa())){
                    subtotal = subtotal.add(detalle.getSubtotal());
                }
            }
            impuesto.setEtiqueta("Subtotal " + tarifa.getDescripcion());
            impuesto.setBaseImponible(subtotal);
            impuesto.setPedido(pedido);
            impuesto.setImpuestoTarifa(tarifa);
            impuesto.setPorcentaje(new BigDecimal(tarifa.getPorcentaje()));
            impuesto.setValor((impuesto.getBaseImponible().multiply(impuesto.getPorcentaje().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
            pedido.getPedidoImpuestoTarifaLst().add(impuesto);
        }
    }

    public void calcularTotal(){
        BigDecimal total = BigDecimal.ZERO;
        for(PedidoDetalle pedidoDetalle : this.pedido.getPedidoDetalleLst()){
            total = total.add(pedidoDetalle.getSubtotal());
        }
        BigDecimal valorIvaFactura = BigDecimal.ZERO;
        for(PedidoImpuestoTarifa impuesto : pedido.getPedidoImpuestoTarifaLst()){
            valorIvaFactura = valorIvaFactura.add(impuesto.getValor());

        }
        pedido.setIva(valorIvaFactura);
        pedido.setTotal(total);
        pedido.setTotalGeneral(valorIvaFactura.add(pedido.getTotal()));
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

    public void choosePedidoWhatsApp() {
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .resizable(false)
                .draggable(false)
                .modal(true)
                .build();
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarPedidoWhatsApp", options, null);
    }

    public void onPedidoWhatsAppChosen(SelectEvent event) {
        //EN CONSTRUCCION
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
