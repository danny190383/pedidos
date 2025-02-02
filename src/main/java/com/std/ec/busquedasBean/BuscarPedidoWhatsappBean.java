package com.std.ec.busquedasBean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("buscarPedidoWhatsappBean")
@ViewScoped
public class BuscarPedidoWhatsappBean implements Serializable {
	private static final long serialVersionUID = 40L;

    @PostConstruct
    public void init(){
    }
}
