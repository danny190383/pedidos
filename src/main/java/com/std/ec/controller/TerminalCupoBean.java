package com.std.ec.controller;

import com.std.ec.entity.*;
import com.std.ec.service.impl.ITerminalEstacionService;
import com.std.ec.service.impl.ITerminalService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("terminalCupoBean")
@ViewScoped
public class TerminalCupoBean implements Serializable {
	private static final long serialVersionUID = 9L;
    @Autowired
    private ITerminalService terminalService;
    @Autowired
    private ITerminalEstacionService estacionServicioService;
    @Inject
    private UserSessionBean userSession;

    private List<Terminal> listTerminales;
    private List<TerminalEstacion> listEstacionServicios;
    private List<TipoCombustible> listTipoCombustible;
    private TerminalEstacion terminalEstacionSlc;
    private TerminalEstacionCupo terminalEstacionCupo;
    private Boolean editando;
    private Long idTerminal;
    private Long idTerminalEstacion;
    private List<String> meses;

    public TerminalCupoBean() {
        this.terminalEstacionSlc = new TerminalEstacion();
        this.listTerminales = new ArrayList<>();
        this.listEstacionServicios = new ArrayList<>();
        this.listTipoCombustible = new ArrayList<>();
        this.terminalEstacionCupo = new TerminalEstacionCupo();
        this.terminalEstacionCupo.setTipoCombustible(new TipoCombustible());
    }

    @PostConstruct
    public void init(){
        this.editando = false;
        this.listTerminales = this.terminalService.findAllWithRelations();
        meses = Arrays.asList(new DateFormatSymbols().getMonths());
        meses = meses.subList(0, 12);
    }

    public void seleccionarTerminal(){
        this.listEstacionServicios.clear();
        this.terminalEstacionSlc = new TerminalEstacion();
        this.idTerminalEstacion = null;
        this.listEstacionServicios = estacionServicioService.listarTerminalEstacionesActivasPorTerminal(idTerminal);
    }

    public void seleccionarTerminalEstacion(){
        if(idTerminalEstacion != null){
            this.terminalEstacionSlc = listEstacionServicios.stream()
                    .filter(tc -> tc.getIdTerminalEstacion() != null && tc.getIdTerminalEstacion().equals(idTerminalEstacion))
                    .findFirst()
                    .orElse(null);

            this.listTipoCombustible.clear();
            this.listTipoCombustible.addAll(this.terminalEstacionSlc.getTerminal().getTipoCombustibleList());
        }else{
            this.terminalEstacionSlc = new TerminalEstacion();
        }
    }

    public void nuevoTerminalEstacionCupo(){
        this.terminalEstacionCupo = new TerminalEstacionCupo();
        this.terminalEstacionCupo.setTipoCombustible(new TipoCombustible());
        this.terminalEstacionCupo.setTerminalEstacion(terminalEstacionSlc);
        this.terminalEstacionCupo.setFechaRegistro(LocalDateTime.now() );
        this.terminalEstacionCupo.setUsuarioRegistra(userSession.getUsuario());
        this.editando = false;
    }

    public void seleccionarCupo(int index){
        List<TerminalEstacionCupo> list = new ArrayList<>(terminalEstacionSlc.getTerminalEstacionCupoLst());
        this.terminalEstacionCupo = list.get(index);
        this.editando = true;
    }

    public void agregarCupo() {
        terminalEstacionCupo.setTipoCombustible(listTipoCombustible.stream()
                .filter(tc -> tc.getIdTipoCombustible().equals(terminalEstacionCupo.getTipoCombustible().getIdTipoCombustible()))
                .findFirst()
                .orElse(null));
        if(!this.editando){
            terminalEstacionSlc.getTerminalEstacionCupoLst().add(terminalEstacionCupo);
        }else{
            TerminalEstacionCupo anterior = terminalEstacionSlc.getTerminalEstacionCupoLst().stream()
                    .filter(c -> c.getIdTerminalEstacionCupo().equals(terminalEstacionCupo.getIdTerminalEstacionCupo()))
                    .findFirst()
                    .orElse(null);

            if (anterior != null) {
                terminalEstacionSlc.getTerminalEstacionCupoLst().remove(anterior);
            }
            terminalEstacionSlc.getTerminalEstacionCupoLst().add(terminalEstacionCupo);
        }
        this.guardar();
        this.editando = false;
    }

    public void guardar(){
        try {
            this.terminalEstacionSlc = estacionServicioService.guardarTerminalEstacion(this.terminalEstacionSlc);
            PrimeFaces.current().executeScript("PF('dlgEstacionServicio').hide();");
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void calcularCupoDia() {
        if (terminalEstacionCupo.getCupoMensual() != null) {
            // Obtener los meses del sistema (asegurándonos de que solo sean los 12 primeros)
            List<String> meses = Arrays.asList(new DateFormatSymbols().getMonths()).subList(0, 12);

            // Obtener el año y el mes desde el string "2025 - enero"
            String[] partes = terminalEstacionCupo.getMes().split(" - ");
            int anio = Integer.parseInt(partes[0]);
            String nombreMes = partes[1].toLowerCase();

            // Buscar el índice del mes en la lista (el índice es el número del mes - 1)
            int numeroMes = meses.indexOf(nombreMes) + 1;

            // Si el mes no se encuentra, lanzar un error
            if (numeroMes == 0) {
                throw new IllegalArgumentException("Mes no reconocido: " + nombreMes);
            }

            // Obtener el número de días del mes
            int diasDelMes = YearMonth.of(anio, numeroMes).lengthOfMonth();

            // Calcular el cupo diario
            terminalEstacionCupo.setCupoDiario(
                    terminalEstacionCupo.getCupoMensual()
                            .divide(new BigDecimal(diasDelMes), 2, RoundingMode.HALF_UP)
            );
        }
        this.calcularVolumenes();
    }

    public void calcularVolumenes(){
        if(terminalEstacionCupo.getPorcentajeMercado() != null &&
           terminalEstacionCupo.getCupoDiario() != null ){
            BigDecimal porcentajeDecimal = terminalEstacionCupo.getPorcentajeMercado().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
            terminalEstacionCupo.setVolumenDiario(
                    porcentajeDecimal.multiply(terminalEstacionCupo.getCupoDiario())
                            .setScale(2, RoundingMode.HALF_UP)
            );

            terminalEstacionCupo.setVolumenSemanal(
                    terminalEstacionCupo.getVolumenDiario().multiply(new BigDecimal(7))
                            .setScale(2, RoundingMode.HALF_UP)
            );

            terminalEstacionCupo.setVolumenMensual(
                    terminalEstacionCupo.getVolumenDiario().multiply(new BigDecimal(30))
                            .setScale(2, RoundingMode.HALF_UP)
            );
        }
    }

    public int getCurrentYear() {
        return Year.now().getValue();
    }

    public List<Terminal> getListTerminales() {
        return listTerminales;
    }

    public void setListTerminales(List<Terminal> listTerminales) {
        this.listTerminales = listTerminales;
    }

    public List<TerminalEstacion> getListEstacionServicios() {
        return listEstacionServicios;
    }

    public void setListEstacionServicios(List<TerminalEstacion> listEstacionServicios) {
        this.listEstacionServicios = listEstacionServicios;
    }

    public TerminalEstacion getTerminalEstacionSlc() {
        return terminalEstacionSlc;
    }

    public void setTerminalEstacionSlc(TerminalEstacion terminalEstacionSlc) {
        this.terminalEstacionSlc = terminalEstacionSlc;
    }

    public Long getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(Long idTerminal) {
        this.idTerminal = idTerminal;
    }

    public List<TipoCombustible> getListTipoCombustible() {
        return listTipoCombustible;
    }

    public void setListTipoCombustible(List<TipoCombustible> listTipoCombustible) {
        this.listTipoCombustible = listTipoCombustible;
    }

    public TerminalEstacionCupo getTerminalEstacionCupo() {
        return terminalEstacionCupo;
    }

    public void setTerminalEstacionCupo(TerminalEstacionCupo terminalEstacionCupo) {
        this.terminalEstacionCupo = terminalEstacionCupo;
    }

    public Long getIdTerminalEstacion() {
        return idTerminalEstacion;
    }

    public void setIdTerminalEstacion(Long idTerminalEstacion) {
        this.idTerminalEstacion = idTerminalEstacion;
    }

    public Boolean getEditando() {
        return editando;
    }

    public void setEditando(Boolean editando) {
        this.editando = editando;
    }

    public List<String> getMeses() {
        return meses;
    }

    public void setMeses(List<String> meses) {
        this.meses = meses;
    }
}
