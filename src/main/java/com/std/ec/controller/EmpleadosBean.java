package com.std.ec.controller;

import com.std.ec.entity.Empleado;
import com.std.ec.entity.Persona;
import com.std.ec.entity.Rol;
import com.std.ec.entity.Usuario;
import com.std.ec.service.impl.IEmpleadoService;
import com.std.ec.service.impl.IPersonaService;
import com.std.ec.service.impl.IRolService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("empleadosBean")
@ViewScoped
public class EmpleadosBean implements Serializable {
	private static final long serialVersionUID = 8L;
    @Autowired
    private IEmpleadoService empleadoService;
    @Autowired
    private IPersonaService personaService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IRolService rolService;

    private List<Empleado> listEmpleados;
    private Empleado empleado;
    private List<Rol> rolesSistemaLst;
    private List<Rol> rolesSistemaLstSlc;
    private Boolean nuevoRegistro;

    public EmpleadosBean() {
        this.listEmpleados = new ArrayList<>();
        this.empleado = new Empleado();
        this.rolesSistemaLst = new ArrayList<>();
        this.rolesSistemaLstSlc = new ArrayList<>();
    }

    @PostConstruct
    public void init(){
        this.listEmpleados = this.empleadoService.listar();
        this.nuevoRegistro = false;
    }

    public void cargarRolesSistemaList() {
        rolesSistemaLst = rolService.listar();
        if(this.empleado.getUsuario() != null){
            if(this.empleado.getUsuario().getRoles() != null){
                rolesSistemaLstSlc = new ArrayList<>(this.empleado.getUsuario().getRoles());
            }
        }
    }

    public void nuevo(){
        empleado = new Empleado();
        empleado.setPersona(new Persona());
        empleado.setFechaRegistro(new Date());
        this.cargarRolesSistemaList();
        this.nuevoRegistro = true;
    }

    public void seleccionar(Empleado empleadoSlc){
        this.empleado = empleadoSlc;
        this.cargarRolesSistemaList();
        this.nuevoRegistro = false;
    }

    public void guardar(){
        if(rolesSistemaLstSlc.isEmpty()){
            FacesUtils.addErrorMessage("Debe asignar un rol.");
            return;
        }
        try {
            if(empleado.getPersona().getIdPersona() == null){
                personaService.guardarPersona(empleado.getPersona());
            }
            if(this.empleado.getUsuario() == null){
                Usuario usuario = new Usuario();
                usuario.getRoles().addAll(this.rolesSistemaLstSlc);
                usuario.setEmpleado(empleado);
                usuario.setEstado(Boolean.TRUE);
                usuario.setUsername(empleado.getPersona().getCedula());
                usuario.setPassword(passwordEncoder.encode(empleado.getPersona().getCedula()));
                empleado.setUsuario(usuario);
            }else {
                this.empleado.getUsuario().getRoles().clear();
                this.empleado.getUsuario().getRoles().addAll(rolesSistemaLstSlc);
            }
            empleadoService.guardarEmpleado(this.empleado);
            if(this.nuevoRegistro){
                this.listEmpleados.add(empleado);
            }
            PrimeFaces.current().executeScript("PF('dlgEmpleado').hide();");
            this.init();
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void eliminar(int index){
        try {
            empleadoService.eliminarEmpleado(this.listEmpleados.get(index));
            this.listEmpleados.remove(index);
            FacesUtils.addInfoMessage("Registro eliminado.");
        }catch (Exception e) {
            FacesUtils.addErrorMessage("Error al eliminar el registro.");
        }
    }

    public void verificarCedulaSistema()
    {
        try {
            Empleado empleadoTmp = this.empleadoService.buscarCedula(this.empleado.getPersona().getCedula());
            if(empleadoTmp == null)
            {
                Persona personaTmp = this.personaService.buscarCedula(this.empleado.getPersona().getCedula());
                if(personaTmp != null)
                {
                   empleado.setPersona(personaTmp);
                }
            }
            else
            {
                empleado = empleadoTmp;
            }
        } catch (Exception e) {
            empleado = new Empleado();
        }
    }

    public List<Empleado> getListEmpleados() {
        return listEmpleados;
    }

    public void setListEmpleados(List<Empleado> listEmpleados) {
        this.listEmpleados = listEmpleados;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public List<Rol> getRolesSistemaLst() {
        return rolesSistemaLst;
    }

    public void setRolesSistemaLst(List<Rol> rolesSistemaLst) {
        this.rolesSistemaLst = rolesSistemaLst;
    }

    public List<Rol> getRolesSistemaLstSlc() {
        return rolesSistemaLstSlc;
    }

    public void setRolesSistemaLstSlc(List<Rol> rolesSistemaLstSlc) {
        this.rolesSistemaLstSlc = rolesSistemaLstSlc;
    }
}
