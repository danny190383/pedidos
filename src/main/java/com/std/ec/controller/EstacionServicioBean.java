package com.std.ec.controller;

import com.std.ec.entity.Canton;
import com.std.ec.entity.Empleado;
import com.std.ec.entity.EstacionServicio;
import com.std.ec.entity.Parroquia;
import com.std.ec.entity.Provincia;
import com.std.ec.service.impl.ICantonService;
import com.std.ec.service.impl.IEstacionServicioService;
import com.std.ec.service.impl.IParroquiaService;
import com.std.ec.service.impl.IProvinciaService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("estacionServicioBean")
@ViewScoped
public class EstacionServicioBean implements Serializable {
	private static final long serialVersionUID = 9L;
    @Autowired
    private IEstacionServicioService estacionServicioService;
    @Autowired
    private IProvinciaService provinciaService;
    @Autowired
    private ICantonService cantonService;
    @Autowired
    private IParroquiaService parroquiaService;

    private List<EstacionServicio> listEstacionServicios;
    private EstacionServicio estacionServicio;
    private Provincia provincia;
    private Canton canton;
    private List<Provincia> listaProvincias;
    private List<Canton> listaCantones;
    private List<Parroquia> listaParroquias;
    private Boolean nuevoRegistro;

    public EstacionServicioBean() {
        this.listEstacionServicios = new ArrayList<>();
        this.listaCantones = new ArrayList<>();
        this.listaParroquias = new ArrayList<>();
        this.listaProvincias = new ArrayList<>();
        this.estacionServicio = new EstacionServicio();
        this.provincia = new Provincia();
        this.canton = new Canton();
    }

    @PostConstruct
    public void init(){
        try {
            this.nuevoRegistro = false;
            this.listEstacionServicios = this.estacionServicioService.listar();
            this.listaProvincias = this.provinciaService.listar(Optional.of("1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void nuevo(){
        this.estacionServicio = new EstacionServicio();
        this.provincia = new Provincia();
        this.canton = new Canton();
        this.nuevoRegistro = true;
    }

    public void seleccionar(EstacionServicio estacionServicioSlc){
        this.nuevoRegistro = false;
        this.estacionServicio = estacionServicioSlc;
        this.provincia = this.estacionServicio.getParroquia().getCanton().getProvincia();
        this.canton = this.estacionServicio.getParroquia().getCanton();
        this.llenarCantones();
        this.llenarParroquias();
    }

    public void guardar(){
        try {
            estacionServicioService.guardarEstacionServicio(this.estacionServicio);
            if(this.nuevoRegistro){
                this.listEstacionServicios.add(estacionServicio);
            }
            PrimeFaces.current().executeScript("PF('dlgEstacionServicio').hide();");
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void eliminar(int index){
        try {
            estacionServicioService.eliminarEstacionServicio(this.listEstacionServicios.get(index));
            this.listEstacionServicios.remove(index);
            FacesUtils.addInfoMessage("Registro eliminado.");
        }catch (Exception e) {
            FacesUtils.addErrorMessage("Error al eliminar el registro.");
        }
    }

    public void actualizarProvincia(AjaxBehaviorEvent event) {
        this.llenarCantones();
        this.estacionServicio.setParroquia(null);
        this.canton = null;
        this.listaParroquias.clear();
    }

    public void actualizarCanton(AjaxBehaviorEvent event) {
        this.llenarParroquias();
        this.estacionServicio.setParroquia(null);
    }

    private void llenarCantones() {
        this.listaCantones.clear();
        this.listaCantones = this.cantonService.listarCantonesProvincia(Optional.of("1"),Optional.of(provincia.getIdProvincia()));
    }

    private void llenarParroquias() {
        this.listaParroquias.clear();
        this.listaParroquias = this.parroquiaService.listarParroquiasCanton(Optional.of("1"),Optional.of(provincia.getIdProvincia()),Optional.of(canton.getCantonPK().getIdCanton()));
    }

    public void chooseEmpleado() {
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .resizable(false)
                .draggable(false)
                .modal(true)
                .build();
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarEmpleados", options, null);
    }

    public void onEmpleadoChosen(SelectEvent event) {
        Empleado empleado = (Empleado) event.getObject();
        if(empleado != null){
            this.estacionServicio.setResponsable(empleado);
        }
    }

    public List<EstacionServicio> getListEstacionServicios() {
        return listEstacionServicios;
    }

    public void setListEstacionServicios(List<EstacionServicio> listEstacionServicios) {
        this.listEstacionServicios = listEstacionServicios;
    }

    public EstacionServicio getEstacionServicio() {
        return estacionServicio;
    }

    public void setEstacionServicio(EstacionServicio estacionServicio) {
        this.estacionServicio = estacionServicio;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public List<Provincia> getListaProvincias() {
        return listaProvincias;
    }

    public void setListaProvincias(List<Provincia> listaProvincias) {
        this.listaProvincias = listaProvincias;
    }

    public List<Canton> getListaCantones() {
        return listaCantones;
    }

    public void setListaCantones(List<Canton> listaCantones) {
        this.listaCantones = listaCantones;
    }

    public List<Parroquia> getListaParroquias() {
        return listaParroquias;
    }

    public void setListaParroquias(List<Parroquia> listaParroquias) {
        this.listaParroquias = listaParroquias;
    }
}
