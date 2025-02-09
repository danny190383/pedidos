package com.std.ec.controller;

import com.std.ec.entity.Transportista;
import com.std.ec.entity.Persona;
import com.std.ec.service.impl.ITransportistaService;
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

@Component("transportistaBean")
@ViewScoped
public class TransportistaBean implements Serializable {
    private static final long serialVersionUID = 6L;

    @Autowired
    private ITransportistaService transportistaService;
    @Autowired
    private IPersonaService personaService;

    private LazyDataModel<Transportista> listTransportista;
    private Transportista transportista;
    private Persona persona;

    public TransportistaBean() {
        this.transportista = new Transportista();
        this.persona = new Persona();
    }

    @PostConstruct
    public void init(){
        listTransportista = new LazyDataModel<Transportista>() {
            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return (int) transportistaService.countTransportistas(filterBy);
            }

            @Override
            public List<Transportista> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return transportistaService.getTransportistas(first, pageSize, sortBy, filterBy);
            }
        };
    }

    public void nuevo(){
        transportista = new Transportista();
    }

    public void nuevoPersona(){
        this.persona = new Persona();
    }

    public void seleccionar(Transportista TransportistaSlc){
        this.transportista = TransportistaSlc;
    }

    public void guardar(){
        try {
            transportistaService.guardarTransportista(this.transportista);
            PrimeFaces.current().executeScript("PF('dlgCamion').hide();");
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void guardarPersona(){
        try {
            personaService.guardarPersona(this.persona);
            this.transportista.setPersona(persona);
            PrimeFaces.current().executeScript("PF('dlgEmpleado').hide();");
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void eliminar(int index){
        try {
            transportistaService.eliminarTransportista(this.listTransportista.getWrappedData().get(index));
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
            this.transportista.setPersona(persona);
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

    public LazyDataModel<Transportista> getListTransportista() {
        return listTransportista;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
