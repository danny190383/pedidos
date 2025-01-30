package com.std.ec.controller;

import com.std.ec.entity.Camion;
import com.std.ec.entity.Persona;
import com.std.ec.service.impl.ICamionService;
import com.std.ec.service.impl.IPersonaService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component("camionBean")
@ViewScoped
public class CamionBean implements Serializable {
    private static final long serialVersionUID = 6L;

    @Autowired
    private ICamionService camionService;
    @Autowired
    private IPersonaService personaService;

    private LazyDataModel<Camion> listCamion;
    private Camion camion;
    private Persona persona;

    public CamionBean() {
        this.camion = new Camion();
        this.persona = new Persona();
    }

    @PostConstruct
    public void init(){
        listCamion = new LazyDataModel<Camion>() {
            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return (int) camionService.countCamiones(filterBy);
            }

            @Override
            public List<Camion> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return camionService.getCamiones(first, pageSize, sortBy, filterBy);
            }
        };
    }

    public void nuevo(){
        camion = new Camion();
    }

    public void nuevoPersona(){
        this.persona = new Persona();
    }

    public void seleccionar(Camion camionSlc){
        this.camion = camionSlc;
    }

    public void guardar(){
        try {
            camionService.guardarCamion(this.camion);
            PrimeFaces.current().executeScript("PF('dlgCamion').hide();");
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void guardarPersona(){
        try {
            personaService.guardarPersona(this.persona);
            this.camion.setPersona(persona);
            PrimeFaces.current().executeScript("PF('dlgEmpleado').hide();");
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void eliminar(int index){
        try {
            camionService.eliminarCamion(this.listCamion.getWrappedData().get(index));
            FacesUtils.addInfoMessage("Registro eliminado.");
        }catch (Exception e) {
            FacesUtils.addErrorMessage("Error al eliminar el registro.");
        }
    }

    public void choosePersona() {
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .resizable(false)
                .draggable(false)
                .modal(true)
                .build();
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarPersona", options, null);
    }

    public void onPersonaChosen(SelectEvent event) {
        Persona persona = (Persona) event.getObject();
        if(persona != null){
            this.camion.setPersona(persona);
        }
    }

    public void verificarCedulaSistema()
    {
        try {
            Persona personaTmp = this.personaService.buscarCedula(this.persona.getCedula());
            if(personaTmp != null) {
            	this.persona = personaTmp;
            }
        } catch (Exception e) {
            this.nuevoPersona();
        }
    }

    public LazyDataModel<Camion> getListCamion() {
        return listCamion;
    }

    public Camion getCamion() {
        return camion;
    }

    public void setCamion(Camion camion) {
        this.camion = camion;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
