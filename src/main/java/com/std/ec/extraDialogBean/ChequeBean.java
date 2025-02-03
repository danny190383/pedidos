package com.std.ec.extraDialogBean;

import com.std.ec.controller.UserSessionBean;
import com.std.ec.entity.*;
import com.std.ec.service.impl.IPedidoService;
import com.std.ec.util.FacesUtils;
import com.std.ec.util.NumberToWordsConverter;
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

@Named("chequeBean")
@ViewScoped
public class ChequeBean implements Serializable {
	private static final long serialVersionUID = 42L;

    @Autowired
    private IPedidoService pedidoService;
    @Inject
    private UserSessionBean userSession;

    private Pedido pedido;
    private Cheque cheque;
    private Boolean editando;
    private int index;

    public ChequeBean() {
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
        this.editando = false;
    }

    public void seleccionar(int index){
        this.cheque = this.pedido.getPedidoChequeLst().get(index);
        this.index = index;
        this.editando = true;
    }

    public void agregarCheque() {
        cheque.setFechaRegistro(LocalDateTime.now());
        cheque.setUsuarioRegistra(userSession.getUsuario());
        if(!this.editando){
            cheque.setPedido(pedido);
            cheque.setEstado(true);
            pedido.getPedidoChequeLst().add(cheque);
        }else{
            pedido.getPedidoChequeLst().set(index, cheque);
        }
        this.cheque = new Cheque();
        this.editando = false;
    }
    
    public void guardar(boolean accion ) {
        if(accion){
            if(pedido.getPedidoChequeLst().isEmpty()){
                FacesUtils.addErrorMessage("No ha registrado cheque.");
                return;
            }
            if(!pedido.estaChequeGenerado()){
                PedidoEstado pedidoEstado = new PedidoEstado();
                pedidoEstado.setPedido(pedido);
                pedidoEstado.setFechaRegistro(new Date());
                pedidoEstado.setUsuarioRegistra(userSession.getUsuario());
                pedidoEstado.setEstadoPedido(new EstadoPedido(Pedido.CHEQUE_GENERADO));
                pedido.getPedidoEstadoLst().add(pedidoEstado);
            }
            PrimeFaces.current().dialog().closeDynamic(pedido);
        }else {
            PrimeFaces.current().dialog().closeDynamic(null);
        }
    }

    public String getMontoLetras() {
        if (cheque.getMonto() != null) {
            cheque.setMontoLetras(NumberToWordsConverter.convertir(cheque.getMonto()));
            return cheque.getMontoLetras();
        }
        return "";
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
}
