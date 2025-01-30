package com.std.ec.controller;

import com.std.ec.entity.Usuario;
import com.std.ec.service.impl.IUsuarioService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component("usuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable {
	private static final long serialVersionUID = 3L;
    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private List<Usuario> listUsuarios;

    public UsuarioBean() {
        listUsuarios = new ArrayList<>();
    }

    @PostConstruct
    public void init(){
        listUsuarios = usuarioService.listar();
    }

    public void resetClave(Usuario usuario){
        usuario.setPassword(passwordEncoder.encode(usuario.getEmpleado().getPersona().getCedula()));
        this.guardar(usuario);
    }

    public void cambiarEstado(Usuario usuario){
        if(usuario.isEstado()){
            usuario.setEstado(false);
        }else {
            usuario.setEstado(true);
        }
        this.guardar(usuario);
    }

    public void guardar(Usuario usuario){
        try {
            usuarioService.guardarUsuario(usuario);
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public List<Usuario> getListUsuarios() {
        return listUsuarios;
    }

    public void setListUsuarios(List<Usuario> listUsuarios) {
        this.listUsuarios = listUsuarios;
    }
}
