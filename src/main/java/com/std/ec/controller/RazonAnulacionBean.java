package com.std.ec.controller;

import com.std.ec.entity.RazonAnulacion;
import com.std.ec.service.impl.IRazonAnulacionService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component("razonAnulacionBean")
@ViewScoped
public class RazonAnulacionBean  implements Serializable {
	private static final long serialVersionUID = 6L;
    @Autowired
    private IRazonAnulacionService razonAnulacionService;

    private List<RazonAnulacion> listRazonAnulacion;
    private RazonAnulacion razonAnulacion;
    private Boolean nuevoRegistro;

    public RazonAnulacionBean() {
        listRazonAnulacion = new ArrayList<>();
    }

    @PostConstruct
    public void init(){
        listRazonAnulacion = razonAnulacionService.listar();
        razonAnulacion = new RazonAnulacion();
        this.nuevoRegistro = false;
    }

    public void nuevo(){
        razonAnulacion = new RazonAnulacion();
        this.nuevoRegistro = true;
    }

    public void seleccionar(RazonAnulacion razonAnulacionSlc){
        this.razonAnulacion = razonAnulacionSlc;
        this.nuevoRegistro = false;
    }

    public void guardar(){
        try {
            razonAnulacionService.guardarRazonAnulacion(this.razonAnulacion);
            PrimeFaces.current().executeScript("PF('dlgRazonAnulacion').hide();");
            FacesUtils.addInfoMessage("Registro guardado.");
            if(this.nuevoRegistro){
                this.listRazonAnulacion.add(razonAnulacion);
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void eliminar(int index){
        try {
            razonAnulacionService.eliminarRazonAnulacion(this.listRazonAnulacion.get(index));
            this.listRazonAnulacion.remove(index);
            FacesUtils.addInfoMessage("Registro eliminado.");
        }catch (Exception e) {
            FacesUtils.addErrorMessage("Error al eliminar el registro.");
        }
    }

    public List<RazonAnulacion> getListRazonAnulacion() {
        return listRazonAnulacion;
    }

    public void setListRazonAnulacion(List<RazonAnulacion> listRazonAnulacion) {
        this.listRazonAnulacion = listRazonAnulacion;
    }

    public RazonAnulacion getRazonAnulacion() {
        return razonAnulacion;
    }

    public void setRazonAnulacion(RazonAnulacion razonAnulacion) {
        this.razonAnulacion = razonAnulacion;
    }
}
