package com.std.ec.busquedasBean;

import com.std.ec.entity.Empleado;
import com.std.ec.service.impl.IEmpleadoService;
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

@Named("buscarEmpleadoBean")
@ViewScoped
public class BuscarEmpleadoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
    private IEmpleadoService empleadoService;

    private LazyDataModel<Empleado> lazyModel;

    @PostConstruct
    public void init(){
        lazyModel = new LazyDataModel<Empleado>() {
            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return (int) empleadoService.countEmpleados(filterBy);
            }

            @Override
            public List<Empleado> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return empleadoService.getEmpleados(first, pageSize, sortBy, filterBy);
            }
        };
    }

    public void selectEmpleadoFromDialog(Empleado empleado) {
        PrimeFaces.current().dialog().closeDynamic(empleado);
    }

    public LazyDataModel<Empleado> getLazyModel() {
        return lazyModel;
    }
}
