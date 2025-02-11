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
import java.util.*;

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

    public ChequeBean() {
        cheque = new Cheque();
    }

    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String pedidoId = params.get("pedido");
        if (pedidoId != null && !pedidoId.isEmpty()) {
            pedido = pedidoService.findAllWithRelations(Long.parseLong(pedidoId));
        } else {
            System.out.println("El parámetro 'pedido' es nulo o vacío.");
        }
        this.editando = false;
        this.nuevoCheque();
    }

    public void nuevoCheque(){
        cheque = new Cheque();
        cheque.setPedido(pedido);
        cheque.setEstado(true);
        cheque.setFechaRegistro(LocalDateTime.now());
        cheque.setUsuarioRegistra(userSession.getUsuario());
    }

    public void seleccionar(int index){
        List<Cheque> chequeList = new ArrayList<>(this.pedido.getPedidoChequeLst());
        this.cheque = chequeList.get(index);
        this.editando = true;
    }

    public void agregarCheque() {
        if(!this.editando){
            if(pedido.estaGenerado()){
                pedido.getPedidoChequeLst().add(cheque);
                PedidoEstado pedidoEstado = new PedidoEstado();
                pedidoEstado.setPedido(pedido);
                pedidoEstado.setFechaRegistro(LocalDateTime.now());
                pedidoEstado.setUsuarioRegistra(userSession.getUsuario());
                pedidoEstado.setEstadoPedido(new EstadoPedido(Pedido.CHEQUE_GENERADO));
                pedido.getPedidoEstadoLst().add(pedidoEstado);
                pedido.setEstadoPrioritario(new EstadoPedido(Pedido.CHEQUE_GENERADO));
            }
            if(pedido.estaChequeAnulado()){
                if(pedido.getPedidoChequeLst().stream().anyMatch(cheque -> cheque.getEstado())){
                    PedidoEstado pedidoEstado = new PedidoEstado();
                    pedidoEstado.setPedido(pedido);
                    pedidoEstado.setFechaRegistro(LocalDateTime.now());
                    pedidoEstado.setUsuarioRegistra(userSession.getUsuario());
                    pedidoEstado.setEstadoPedido(new EstadoPedido(Pedido.CHEQUE_GENERADO));
                    pedido.getPedidoEstadoLst().add(pedidoEstado);
                    pedido.setEstadoPrioritario(new EstadoPedido(Pedido.CHEQUE_GENERADO));
                }
            }
        }else{
			Cheque chequeAnterior = pedido.getPedidoChequeLst().stream()
	                .filter(c -> c.getIdCheque().equals(cheque.getIdCheque()))
	                .findFirst()
	                .orElse(null);
	        if (chequeAnterior != null) {
	            pedido.getPedidoChequeLst().remove(chequeAnterior);
	        }
	        pedido.getPedidoChequeLst().add(cheque);
        }
        this.nuevoCheque();
        this.editando = false;
    }
    
    public void guardar(boolean accion ) {
        if(accion){
            if(pedido.getPedidoChequeLst().isEmpty()){
                FacesUtils.addErrorMessage("No ha registrado cheque.");
                return;
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
