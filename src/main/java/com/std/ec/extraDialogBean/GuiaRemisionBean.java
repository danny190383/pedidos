package com.std.ec.extraDialogBean;

import com.std.ec.controller.UserSessionBean;
import com.std.ec.entity.*;
import com.std.ec.service.impl.IPedidoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Named("guiaRemisionBean")
@ViewScoped
public class GuiaRemisionBean implements Serializable {
	private static final long serialVersionUID = 44L;

    @Autowired
    private IPedidoService pedidoService;
    @Inject
    private UserSessionBean userSession;

    private Pedido pedido;
    private PedidoCamion pedidoCamionSlc;
    private GuiaRemision guiaRemision;
    private Boolean editando;

    public GuiaRemisionBean() {
        guiaRemision = new GuiaRemision();
        pedidoCamionSlc = new PedidoCamion();
    }

    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String pedidoId = params.get("pedido");
        this.editando = false;
        if (pedidoId != null && !pedidoId.isEmpty()) {
            pedido = pedidoService.findAllWithRelations(Long.parseLong(pedidoId));
            if(!pedido.getPedidoCamionActivos().isEmpty()){
                this.pedidoCamionSlc = pedido.getPedidoCamionActivos().get(0);
                if(!this.pedidoCamionSlc.getGuiaRemisionLst().isEmpty()){
                    this.guiaRemision = this.pedidoCamionSlc.getGuiaRemisionLst().get(0);
                    this.editando = true;
                }
            }
        } else {
            System.out.println("El parámetro 'pedido' es nulo o vacío.");
        }
    }
    
    public void guardar(boolean accion ) {
        if(accion){
            if(editando){
                guiaRemision.setFechaRegistro(LocalDateTime.now());
                guiaRemision.setUsuarioRegistra(userSession.getUsuario());
                pedidoCamionSlc.getGuiaRemisionLst().set(0, guiaRemision);
            }else{
                guiaRemision.setFechaRegistro(LocalDateTime.now());
                guiaRemision.setUsuarioRegistra(userSession.getUsuario());
                guiaRemision.setPedidoCamion(pedidoCamionSlc);
                pedidoCamionSlc.getGuiaRemisionLst().add(guiaRemision);
            }
            if(!pedido.estaDespachado()){
                PedidoEstado pedidoEstado = new PedidoEstado();
                pedidoEstado.setPedido(pedido);
                pedidoEstado.setFechaRegistro(new Date());
                pedidoEstado.setUsuarioRegistra(userSession.getUsuario());
                pedidoEstado.setEstadoPedido(new EstadoPedido(Pedido.DESPACHADO));
                pedido.getPedidoEstadoLst().add(pedidoEstado);
            }
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

    public GuiaRemision getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(GuiaRemision guiaRemision) {
        this.guiaRemision = guiaRemision;
    }

    public PedidoCamion getPedidoCamionSlc() {
        return pedidoCamionSlc;
    }

    public void setPedidoCamionSlc(PedidoCamion pedidoCamionSlc) {
        this.pedidoCamionSlc = pedidoCamionSlc;
    }
}
