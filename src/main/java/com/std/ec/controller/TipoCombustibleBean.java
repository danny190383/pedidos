package com.std.ec.controller;

import com.std.ec.entity.*;
import com.std.ec.service.ImpuestoService;
import com.std.ec.service.impl.ITipoCombustibleService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("tipoCombustibleBean")
@ViewScoped
public class TipoCombustibleBean implements Serializable {
	private static final long serialVersionUID = 3L;
    @Autowired
    private ITipoCombustibleService tipoCombustibleService;
    @Autowired
    private ImpuestoService impuestoService;
    @Inject
    private UserSessionBean userSession;

    private List<TipoCombustible> listTipoCombustibles;
    private List<Impuesto> listTimpuesto;
    private Impuesto selectedImpuesto;
    private ImpuestoTarifa selectedImpuestoTarifa;
    private TipoCombustible tipoCombustible;
    private BigDecimal costo;
    private Boolean nuevoRegistro;

    public TipoCombustibleBean() {
        listTipoCombustibles = new ArrayList<>();
        listTimpuesto = new ArrayList<>();
        selectedImpuesto = new Impuesto();
        selectedImpuesto.setImpuestoTarifaList(new ArrayList<>());
        this.selectedImpuestoTarifa = new ImpuestoTarifa();
        tipoCombustible = new TipoCombustible();
    }

    @PostConstruct
    public void init(){
        this.nuevoRegistro = false;
        listTipoCombustibles = tipoCombustibleService.listar();
        listTimpuesto = impuestoService.listar();
    }

    public void nuevo(){
        this.tipoCombustible = new TipoCombustible();
        selectedImpuesto = new Impuesto();
        selectedImpuesto.setImpuestoTarifaList(new ArrayList<>());
        this.selectedImpuestoTarifa = new ImpuestoTarifa();
        this.nuevoRegistro = true;
    }

    public void agregarCosto(TipoCombustible tipoCombustibleSlc) {
        if (this.costo == null || this.costo.compareTo(BigDecimal.ZERO) == 0) {
            FacesUtils.addErrorMessage("El costo debe ser mayor que 0.");
            return;
        }
        try {
            TipoCombustibleCosto tipoCombustibleCosto = new TipoCombustibleCosto();
            tipoCombustibleCosto.setCosto(costo);
            tipoCombustibleCosto.setTipoCombustible(tipoCombustibleSlc);
            tipoCombustibleCosto.setEstado(Boolean.TRUE);
            tipoCombustibleCosto.setFechaRegistro(new Date());
            tipoCombustibleCosto.setUsuarioRegistra(userSession.getUsuario());
            tipoCombustibleSlc.getTipoCombustibleCostoLst().forEach(tipoCosto -> tipoCosto.setEstado(Boolean.FALSE));
            tipoCombustibleSlc.getTipoCombustibleCostoLst().add(tipoCombustibleCosto);
            tipoCombustibleService.guardarTipoCombustible(tipoCombustibleSlc);
            this.costo = BigDecimal.ZERO;
            FacesUtils.addInfoMessage("Registro guardado.");
            this.init();
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void seleccionar(TipoCombustible tipoCombustibleSlc){
        this.tipoCombustible = tipoCombustibleSlc;
        this.nuevoRegistro = false;
        selectedImpuesto = new Impuesto();
        selectedImpuesto.setImpuestoTarifaList(new ArrayList<>());
        this.selectedImpuestoTarifa = new ImpuestoTarifa();
    }

    public void guardar(){
        try {
            tipoCombustibleService.guardarTipoCombustible(this.tipoCombustible);
            this.costo = BigDecimal.ZERO;
            if(this.nuevoRegistro){
                this.listTipoCombustibles.add(tipoCombustible);
            }
            PrimeFaces.current().executeScript("PF('dlgTipoCombustible').hide();");
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void agregarImpuesto() {
        ProductoImpuestoTarifa productoImpuestoTarifa = new ProductoImpuestoTarifa();
        productoImpuestoTarifa.setTipoCombustible(tipoCombustible);
        productoImpuestoTarifa.setImpuestoTarifa(this.seleccionarTarifa());
        this.tipoCombustible.getProductoImpuestoTarifaLst().add(productoImpuestoTarifa);
    }

    public void eliminarImpuesto(int index){
        this.tipoCombustible.getProductoImpuestoTarifaLst().remove(index);
        FacesUtils.addInfoMessage("Registro eliminado.");
    }

    public void eliminar(int index){
        try {
            tipoCombustibleService.eliminarTipoCombustible(this.listTipoCombustibles.get(index));
            this.listTipoCombustibles.remove(index);
            FacesUtils.addInfoMessage("Registro eliminado.");
        }catch (Exception e) {
            FacesUtils.addErrorMessage("Error al eliminar el registro.");
        }
    }

    public void seleccionarImpuesto(){
        this.selectedImpuesto =  listTimpuesto.stream()
                .filter(e -> e.getIdImpuesto().equals(this.selectedImpuesto.getIdImpuesto()))
                .findFirst()
                .orElse(null);
    }

    public ImpuestoTarifa seleccionarTarifa(){
        return   selectedImpuesto.getImpuestoTarifaList().stream()
                .filter(e -> e.getIdImpuestoTarifa().equals(this.selectedImpuestoTarifa.getIdImpuestoTarifa()))
                .findFirst()
                .orElse(null);
    }

    public TipoCombustible getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(TipoCombustible tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public List<TipoCombustible> getListTipoCombustibles() {
        return listTipoCombustibles;
    }

    public void setListTipoCombustibles(List<TipoCombustible> listTipoCombustibles) {
        this.listTipoCombustibles = listTipoCombustibles;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Impuesto getSelectedImpuesto() {
        return selectedImpuesto;
    }

    public void setSelectedImpuesto(Impuesto selectedImpuesto) {
        this.selectedImpuesto = selectedImpuesto;
    }

    public ImpuestoTarifa getSelectedImpuestoTarifa() {
        return selectedImpuestoTarifa;
    }

    public void setSelectedImpuestoTarifa(ImpuestoTarifa selectedImpuestoTarifa) {
        this.selectedImpuestoTarifa = selectedImpuestoTarifa;
    }

    public List<Impuesto> getListTimpuesto() {
        return listTimpuesto;
    }

    public void setListTimpuesto(List<Impuesto> listTimpuesto) {
        this.listTimpuesto = listTimpuesto;
    }
}
