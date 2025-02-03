package com.std.ec.extraDialogBean;

import com.std.ec.controller.UserSessionBean;
import com.std.ec.entity.*;
import com.std.ec.service.impl.IPedidoService;
import com.std.ec.service.impl.IRazonAnulacionService;
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
import java.util.List;
import java.util.Map;

@Named("validarChequeBean")
@ViewScoped
public class ValidarChequeBean implements Serializable {
	private static final long serialVersionUID = 42L;

    @Autowired
    private IPedidoService pedidoService;
    @Autowired
    private IRazonAnulacionService razonAnulacionService;
    @Inject
    private UserSessionBean userSession;

    private List<RazonAnulacion> listRazonAnulacion;
    private Pedido pedido;
    private Cheque cheque;
    private Boolean editando;
    private int index;

    public ValidarChequeBean() {
        cheque = new Cheque();
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
        listRazonAnulacion = razonAnulacionService.listarActivasPorTipo("2");
        this.editando = false;
    }

    public void seleccionar(int index){
        this.cheque = this.pedido.getPedidoChequeLst().get(index);
        if(cheque.getRazonAnulacion() == null){
            this.cheque.setRazonAnulacion(new RazonAnulacion());
        }
        this.editando = true;
        this.index = index;
    }

    public void agregarCheque() {
        if(cheque.getEstado()){
            cheque.setRazonAnulacion(null);
            cheque.setUsuarioAnula(null);
            if(!pedido.estaChequeValidado()){
                PedidoEstado pedidoEstado = new PedidoEstado();
                pedidoEstado.setPedido(pedido);
                pedidoEstado.setFechaRegistro(new Date());
                pedidoEstado.setUsuarioRegistra(userSession.getUsuario());
                pedidoEstado.setEstadoPedido(new EstadoPedido(Pedido.CHEQUE_VALIDADO));
                pedido.getPedidoEstadoLst().add(pedidoEstado);
            }
        }else{
            cheque.setFechaAnula(LocalDateTime.now());
            cheque.setUsuarioAnula(userSession.getUsuario());
            cheque.setSecuencialBanco(null);
        }
        pedido.getPedidoChequeLst().set(index, cheque);
        this.cheque = new Cheque();
        this.editando = false;
    }
    
    public void guardar(boolean accion ) {
        if(accion){
            PrimeFaces.current().dialog().closeDynamic(pedido);
        }else {
            PrimeFaces.current().dialog().closeDynamic(null);
        }
    }

    public Cheque getCheque() {
        return cheque;
    }

    public void setCheque(Cheque cheque) {
        this.cheque = cheque;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<RazonAnulacion> getListRazonAnulacion() {
        return listRazonAnulacion;
    }

    public void setListRazonAnulacion(List<RazonAnulacion> listRazonAnulacion) {
        this.listRazonAnulacion = listRazonAnulacion;
    }

    public Boolean getEditando() {
        return editando;
    }

    public void setEditando(Boolean editando) {
        this.editando = editando;
    }
}
