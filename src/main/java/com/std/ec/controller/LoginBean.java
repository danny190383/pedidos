package com.std.ec.controller;


import com.std.ec.entity.Usuario;
import com.std.ec.service.impl.IUsuarioService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.faces.view.ViewScoped;

import java.io.Serializable;

@Component("loginBean")
@ViewScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = 10L;

    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario usuario;

    public LoginBean() {
        this.usuario = new Usuario();
    }

    @PostConstruct
    public void init() {
        this.usuario = usuarioService.findByUsername(this.getUserName()).orElse(null);
    }

    public String getUserName(){
        Object datoUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (datoUser instanceof UserDetails){
            return ((UserDetails)datoUser).getUsername();
        }
        return datoUser.toString();
    }

    public void guardar(){
        try {
            if(!usuario.getPasswordTmp().isEmpty()){
                usuario.setPassword(passwordEncoder.encode(usuario.getPasswordTmp().trim()));
            }
            usuarioService.guardarUsuario(this.usuario);
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
