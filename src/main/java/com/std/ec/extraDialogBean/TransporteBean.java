package com.std.ec.extraDialogBean;

import com.std.ec.controller.UserSessionBean;
import com.std.ec.entity.*;
import com.std.ec.service.impl.ICamionService;
import com.std.ec.service.impl.IPedidoService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named("transporteBean")
@ViewScoped
public class TransporteBean implements Serializable {
	private static final long serialVersionUID = 44L;

    @Autowired
    private IPedidoService pedidoService;
    @Autowired
    private ICamionService camionService;
    @Inject
    private UserSessionBean userSession;

    private Pedido pedido;
    private int index;
    private Camion camion;
    private List<Camion> camionesList;
    private String observacion;

    public TransporteBean() {
        this.camion = new Camion();
        this.camionesList = new ArrayList<>();
    }

    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String pedidoId = params.get("pedido");
        if (pedidoId != null && !pedidoId.isEmpty()) {
            pedido = pedidoService.findPedidoById(Long.parseLong(pedidoId));
        } else {
            System.out.println("El parámetro 'pedido' es nulo o vacío.");
        }
        this.observacion = "";
        this.camionesList.addAll(this.camionService.obtenerCamionesDisponibles(pedido.getTotalVolumen().floatValue()));
    }

    public void seleccionar(int index){
        this.index = index;
    }

    public void agregarTansporte() {
        boolean yaExiste =  pedido.getPedidoCamionLst() != null &&
                            !pedido.getPedidoCamionLst().isEmpty() &&
                            pedido.getPedidoCamionLst().stream()
                        .anyMatch(camionTmp -> camionTmp.getCamion().getIdCamion().equals(camion.getIdCamion()));
        if (yaExiste) {
            FacesUtils.addErrorMessage("Error: El camión ya está agregado en el pedido.");
            return;
        }
        PedidoCamion pedidoCamion = new PedidoCamion();
        pedidoCamion.setCamion(camion);
        pedidoCamion.setPedido(pedido);
        pedidoCamion.setEstado(true);
        pedidoCamion.setFechaRegistro(new Date());
        pedidoCamion.setUsuarioRegistra(userSession.getUsuario());
        pedidoCamion.setObservacion(observacion);
        if(!pedido.estaTransporteAsignado()){
            PedidoEstado pedidoEstado = new PedidoEstado();
            pedidoEstado.setPedido(pedido);
            pedidoEstado.setFechaRegistro(new Date());
            pedidoEstado.setUsuarioRegistra(userSession.getUsuario());
            pedidoEstado.setEstadoPedido(new EstadoPedido(Pedido.TRANSPORTE_ASIGNADO));
            pedido.getPedidoEstadoLst().add(pedidoEstado);
        }
        this.pedido.getPedidoCamionLst().forEach(caminoTmp -> caminoTmp.setEstado(Boolean.FALSE));
        this.pedido.getPedidoCamionLst().add(0, pedidoCamion);
        this.camion = new Camion();
        this.observacion = "";
    }
    
    public void guardar(boolean accion ) {
        if(accion){
            PrimeFaces.current().dialog().closeDynamic(pedido);
        }else {
            PrimeFaces.current().dialog().closeDynamic(null);
        }
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Camion getCamion() {
        return camion;
    }

    public void setCamion(Camion camion) {
        this.camion = camion;
    }

    public List<Camion> getCamionesList() {
        return camionesList;
    }

    public void setCamionesList(List<Camion> camionesList) {
        this.camionesList = camionesList;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
