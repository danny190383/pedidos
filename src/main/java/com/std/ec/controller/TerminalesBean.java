package com.std.ec.controller;

import com.std.ec.entity.*;
import com.std.ec.service.impl.ICantonService;
import com.std.ec.service.impl.IParroquiaService;
import com.std.ec.service.impl.IProvinciaService;
import com.std.ec.service.impl.ITerminalService;
import com.std.ec.service.impl.ITipoCombustibleService;
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

@Component("terminalesBean")
@ViewScoped
public class TerminalesBean implements Serializable {
	private static final long serialVersionUID = 4L;
    @Autowired
    private ITerminalService terminalService;
    @Autowired
    private IProvinciaService provinciaService;
    @Autowired
    private ICantonService cantonService;
    @Autowired
    private IParroquiaService parroquiaService;
    @Autowired
    private ITipoCombustibleService tipoCombustibleService;

    private List<Terminal> listTerminales;
    private Terminal terminal;
    private Provincia provincia;
    private Canton canton;
    private List<Provincia> listaProvincias;
    private List<Canton> listaCantones;
    private List<Parroquia> listaParroquias;
    private List<TipoCombustible> tiposCombustiblesSource ;
    private List<TipoCombustible> tiposCombustiblesTarget;
    private Boolean nuevoRegistro;

    public TerminalesBean() {
        this.listTerminales = new ArrayList<>();
        this.listaCantones = new ArrayList<>();
        this.listaParroquias = new ArrayList<>();
        this.listaProvincias = new ArrayList<>();
        this.terminal = new Terminal();
        this.provincia = new Provincia();
        this.canton = new Canton();
        this.tiposCombustiblesSource = new ArrayList<>();
        this.tiposCombustiblesTarget = new ArrayList<>();
    }

    @PostConstruct
    public void init(){
        try {
            this.nuevoRegistro = false;
            this.listTerminales = this.terminalService.listar();
            this.listaProvincias = this.provinciaService.listar(Optional.of("1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void cargarTiposCombustibles() {
        tiposCombustiblesSource = tipoCombustibleService.listar();
        if(this.terminal.getIdTerminal() != null){
            if(this.terminal.getTipoCombustibleList() != null){
                tiposCombustiblesTarget = new ArrayList<>(this.terminal.getTipoCombustibleList());
            }
        }
    }

    public void nuevo(){
        this.terminal = new Terminal();
        this.provincia = new Provincia();
        this.canton = new Canton();
        this.cargarTiposCombustibles();
        this.nuevoRegistro = true;
    }

    public void seleccionar(Terminal terminalSlc){
        this.nuevoRegistro = false;
        this.terminal = terminalSlc;
        this.provincia = this.terminal.getParroquia().getCanton().getProvincia();
        this.canton = this.terminal.getParroquia().getCanton();
        this.llenarCantones();
        this.llenarParroquias();
        this.cargarTiposCombustibles();
    }

    public void guardar(){
        try {
            this.terminal.getTipoCombustibleList().clear();
            this.terminal.getTipoCombustibleList().addAll(tiposCombustiblesTarget);
            this.terminalService.guardarTerminal(this.terminal);
            if(this.nuevoRegistro){
                this.listTerminales.add(terminal);
            }
            PrimeFaces.current().executeScript("PF('dlgTerminal').hide();");
            FacesUtils.addInfoMessage("Registro guardado.");
            this.init();
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void eliminar(int index){
        try {
            terminalService.eliminarTerminal(this.listTerminales.get(index));
            this.listTerminales.remove(index);
            FacesUtils.addInfoMessage("Registro eliminado.");
        }catch (Exception e) {
            FacesUtils.addErrorMessage("Error al eliminar el registro.");
        }
    }

    public void actualizarProvincia(AjaxBehaviorEvent event) {
        this.llenarCantones();
        this.terminal.setParroquia(null);
        this.canton = null;
        this.listaParroquias.clear();
    }

    public void actualizarCanton(AjaxBehaviorEvent event) {
        this.llenarParroquias();
        this.terminal.setParroquia(null);
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
            this.terminal.setResponsable(empleado);
        }
    }

    public void chooseEstacionServicio(Terminal terminal) {
        this.terminal = terminal;
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .resizable(false)
                .draggable(false)
                .modal(true)
                .build();
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarEstacionServicio", options, null);
    }

    public void onEstacionServicioChosen(SelectEvent event) {
        EstacionServicio estacion = (EstacionServicio) event.getObject();
        if(estacion != null){
            if(!this.terminal.getEstacionServicioList().contains(estacion)){
                this.terminal.getEstacionServicioList().add(estacion);
                this.terminalService.guardarTerminal(this.terminal);
                this.init();
                FacesUtils.addInfoMessage("Registro guardado.");
            }else{
                FacesUtils.addErrorMessage("La estacion de servicio ya existe.");
            }
        }
    }

    public List<Terminal> getListTerminales() {
        return listTerminales;
    }

    public void setListTerminales(List<Terminal> listTerminales) {
        this.listTerminales = listTerminales;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
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

    public List<TipoCombustible> getTiposCombustiblesSource() {
        return tiposCombustiblesSource;
    }

    public void setTiposCombustiblesSource(List<TipoCombustible> tiposCombustiblesSource) {
        this.tiposCombustiblesSource = tiposCombustiblesSource;
    }

    public List<TipoCombustible> getTiposCombustiblesTarget() {
        return tiposCombustiblesTarget;
    }

    public void setTiposCombustiblesTarget(List<TipoCombustible> tiposCombustiblesTarget) {
        this.tiposCombustiblesTarget = tiposCombustiblesTarget;
    }
}
