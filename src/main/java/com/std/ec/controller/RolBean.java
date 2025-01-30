package com.std.ec.controller;

import com.std.ec.service.impl.IRolService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.std.ec.entity.Rol;

@Component("rolBean")
@ViewScoped
public class RolBean implements Serializable {
	private static final long serialVersionUID = 5L;

    @Autowired
    private IRolService rolService;

    private List<Rol> listRoles;
    private Rol rol;
    private Boolean nuevoRegistro;

    public RolBean() {
        listRoles = new ArrayList<>();
    }

    @PostConstruct
    public void init(){
        listRoles = rolService.listar();
        rol = new Rol();
        this.nuevoRegistro = false;
    }

    public void nuevo(){
        rol = new Rol();
        this.nuevoRegistro = true;
    }

    public void seleccionar(Rol rolSlc){
        this.rol = rolSlc;
        this.nuevoRegistro = false;
    }

    public void guardar(){
        try {
            rolService.guardarRol(this.rol);
            PrimeFaces.current().executeScript("PF('dlgRol').hide();");
            FacesUtils.addInfoMessage("Registro guardado.");
            if(this.nuevoRegistro){
                this.listRoles.add(rol);
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void eliminar(int index){
        try {
            rolService.eliminarRol(this.listRoles.get(index));
            this.listRoles.remove(index);
            FacesUtils.addInfoMessage("Registro eliminado.");
        }catch (Exception e) {
            FacesUtils.addErrorMessage("Error al eliminar el registro.");
        }
    }

    public List<Rol> getListRoles() {
        return listRoles;
    }

    public void setListRoles(List<Rol> listRoles) {
        this.listRoles = listRoles;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
