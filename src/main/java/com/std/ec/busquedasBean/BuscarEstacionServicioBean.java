package com.std.ec.busquedasBean;

import com.std.ec.entity.EstacionServicio;
import com.std.ec.service.impl.IEstacionServicioService;
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

@Named("buscarEstacionServicioBean")
@ViewScoped
public class BuscarEstacionServicioBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
    private IEstacionServicioService estacionServicioService;

    private LazyDataModel<EstacionServicio> lazyModel;

    @PostConstruct
    public void init(){
        lazyModel = new LazyDataModel<EstacionServicio>() {
            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return (int) estacionServicioService.countEstacionServicio(filterBy);
            }

            @Override
            public List<EstacionServicio> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return estacionServicioService.getEstacionServicio(first, pageSize, sortBy, filterBy);
            }
        };
    }

    public void selectEstacionServicioServiceFromDialog(EstacionServicio estacionServicio) {
        PrimeFaces.current().dialog().closeDynamic(estacionServicio);
    }

    public LazyDataModel<EstacionServicio> getLazyModel() {
        return lazyModel;
    }
}
