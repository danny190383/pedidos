package com.std.ec.busquedasBean;

import com.std.ec.entity.Pedido;
import com.std.ec.service.impl.IPedidoService;
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

@Named("buscarPedidoBean")
@ViewScoped
public class BuscarPedidoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
    private IPedidoService pedidoServiceS;

    private LazyDataModel<Pedido> lazyModel;

    @PostConstruct
    public void init(){
        lazyModel = new LazyDataModel<Pedido>() {
            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return (int) pedidoServiceS.countPedidos(filterBy);
            }

            @Override
            public List<Pedido> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return pedidoServiceS.getPedidos(first, pageSize, sortBy, filterBy);
            }
        };
    }

    public void selectPersonaFromDialog(Pedido pedido) {
        PrimeFaces.current().dialog().closeDynamic(pedido);
    }

    public LazyDataModel<Pedido> getLazyModel() {
        return lazyModel;
    }
}
