package com.std.ec.extraDialogBean;

import com.std.ec.controller.UserSessionBean;
import com.std.ec.entity.Cheque;
import com.std.ec.entity.Pedido;
import com.std.ec.entity.RazonAnulacion;
import com.std.ec.service.impl.IPedidoService;
import com.std.ec.service.impl.IRazonAnulacionService;
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

    public ValidarChequeBean() {
        cheque = new Cheque();
        cheque.setRazonAnulacion(new RazonAnulacion());
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
    }

    public void seleccionar(Cheque cheque){
        this.cheque = cheque;
    }

    public void agregarCheque( ) {
        cheque.setFechaRegistro(LocalDateTime.now());
        cheque.setUsuarioRegistra(userSession.getUsuario());
        if(cheque.getIdCheque() == null){
            cheque.setPedido(pedido);
            cheque.setEstado(true);
            pedido.getChequeLst().add(cheque);
        }
        this.cheque = new Cheque();
    }
    
    public void guardar(boolean accion ) {
        if(accion){
            PrimeFaces.current().dialog().closeDynamic(cheque);
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

    public List<RazonAnulacion> getListRazonAnulacion() {
        return listRazonAnulacion;
    }

    public void setListRazonAnulacion(List<RazonAnulacion> listRazonAnulacion) {
        this.listRazonAnulacion = listRazonAnulacion;
    }
}
