package com.std.ec.busquedasBean;

import com.std.ec.entity.Persona;
import com.std.ec.entity.Transportista;
import com.std.ec.service.impl.IPersonaService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named("buscarPersonaBean")
@ViewScoped
public class BuscarPersonaBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
    private IPersonaService personaService;

    private LazyDataModel<Persona> lazyModel;
    private Persona persona;

    @PostConstruct
    public void init(){
        lazyModel = new LazyDataModel<Persona>() {
            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return (int) personaService.countPersonas(filterBy);
            }

            @Override
            public List<Persona> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return personaService.getPersonas(first, pageSize, sortBy, filterBy);
            }
        };
    }

    public void guardarPersona(){
        try {
            personaService.guardarPersona(this.persona);
            PrimeFaces.current().executeScript("PF('dlgPersona').hide();");
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void seleccionar(Persona persona){
        this.persona = persona;
    }

    public void nuevo(){
        persona = new Persona();
    }

    public void eliminar(int index){
        try {
            personaService.eliminarPersona(this.lazyModel.getWrappedData().get(index));
            FacesUtils.addInfoMessage("Registro eliminado.");
        }catch (Exception e) {
            FacesUtils.addErrorMessage("Error al eliminar el registro.");
        }
    }

    public void selectPersonaFromDialog(Persona persona) {
        PrimeFaces.current().dialog().closeDynamic(persona);
    }

    public LazyDataModel<Persona> getLazyModel() {
        return lazyModel;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
