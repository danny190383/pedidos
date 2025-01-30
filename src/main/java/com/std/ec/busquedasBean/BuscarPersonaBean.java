package com.std.ec.busquedasBean;

import com.std.ec.entity.Persona;
import com.std.ec.service.impl.IPersonaService;
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

    public void selectPersonaFromDialog(Persona persona) {
        PrimeFaces.current().dialog().closeDynamic(persona);
    }

    public LazyDataModel<Persona> getLazyModel() {
        return lazyModel;
    }
}
