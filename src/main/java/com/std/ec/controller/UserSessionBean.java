package com.std.ec.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.std.ec.entity.Ruta;
import com.std.ec.entity.Usuario;
import com.std.ec.service.impl.IRutaService;
import com.std.ec.service.impl.IUsuarioService;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.enterprise.context.SessionScoped;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component("userSessionBean")
@SessionScoped
public class UserSessionBean implements Serializable {
	private static final long serialVersionUID = 2;

	@Autowired
	private IRutaService ruraService;
	@Autowired
	private IUsuarioService usuarioService;

	private String username;
    private String role;
	private MenuModel menuModulosAcceos;
	private List<Ruta> rutasAcceso;
	private Usuario usuario;

	public void cargarMenuModulosAccesos(){
		this.usuario = usuarioService.findByUsername(username).orElse(null);
		this.menuModulosAcceos = new DefaultMenuModel();
		List<Ruta> listaMenu = this.ruraService.listarPorNivelEStado(1,Boolean.TRUE);
		DefaultSubMenu firstSubmenu = DefaultSubMenu.builder()
				.expanded(true)
				.style("visibility: hidden;")
				.build();
		for (Ruta opcion : listaMenu) {

			DefaultMenuItem item = DefaultMenuItem.builder()
					.icon(opcion.getIcon())
					.title(opcion.getNombre())
					.style("width: 32%")
					.command("#{userSessionBean.cambiarModuloAction("+opcion.getIdRuta()+")}")
					.update("frmMenuSecundario")
					.build();
			firstSubmenu.getElements().add(item);
		}
		this.menuModulosAcceos.getElements().add(firstSubmenu);
	}

	public void cambiarModuloAction(Long codModulo) {
		this.rutasAcceso = new ArrayList<>();
		if(codModulo != null){
			this.rutasAcceso = this.ruraService.listarPorPadreEStado(codModulo, Boolean.TRUE);
		}
	}
}
