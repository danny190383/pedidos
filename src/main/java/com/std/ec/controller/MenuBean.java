package com.std.ec.controller;

import com.std.ec.entity.Rol;
import com.std.ec.entity.Ruta;
import com.std.ec.service.impl.IRolService;
import com.std.ec.service.impl.IRutaService;
import com.std.ec.util.FacesUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("menuBean")
@ViewScoped
public class MenuBean implements Serializable {
	private static final long serialVersionUID = 7L;
    @Autowired
    private IRutaService ruraService;
    @Autowired
    private IRolService rolService;

    private Ruta ruta;
    private TreeNode<Ruta> root;
    private DualListModel<Rol> rolesSistema;

    public MenuBean() {
        this.ruta = new Ruta();
        this.rolesSistema = new DualListModel<>();
    }

    @PostConstruct
    public void init(){
        this.cargarArbol();
        this.cargarRolesSistema();
    }

    private void cargarArbol() {
        this.root = new DefaultTreeNode("Menú", null);
        List<Ruta> principales = this.ruraService.listarPorNivel(Optional.of(1));
        for (Ruta opcion : principales) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, root);
            itemHijo.setExpanded(true);
            llenarHijos(opcion, itemHijo);
        }
    }

    private void llenarHijos(Ruta opc, TreeNode menuPadre) {
        List<Ruta> aux = this.ruraService.listarPorPadre(Optional.of(opc.getIdRuta()));
        for (Ruta opcion : aux) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, menuPadre);
            this.llenarHijos(opcion, itemHijo);
        }
    }

    public void cargarRolesSistema() {
        List<Rol> rolesSistemaSource = new ArrayList<>();
        List<Rol> rolesSistemaTarget = new ArrayList<>();
        rolesSistemaSource = rolService.listar();
        if(this.ruta.getRoles() != null){
            rolesSistemaTarget = new ArrayList<>(this.ruta.getRoles());
            rolesSistemaSource.removeAll(rolesSistemaTarget);
        }
        rolesSistema = new DualListModel<>(rolesSistemaSource, rolesSistemaTarget);
    }

    public void onNodeSelect(NodeSelectEvent event) {
        this.ruta = (Ruta) event.getTreeNode().getData();
        this.cargarRolesSistema();
    }

    public void guardar(){
        try {
            this.ruta.getRoles().clear();
            this.ruta.getRoles().addAll(this.rolesSistema.getTarget());
            this.ruraService.guardarRuta(this.ruta);
            this.ruta = new Ruta();
            this.cargarArbol();
            this.cargarRolesSistema();
            PrimeFaces.current().executeScript("PF('dlgRol').hide();");
            FacesUtils.addInfoMessage("Registro guardado.");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el registro.");
        }
    }

    public void eliminar(){
        ruraService.eliminarRuta(this.ruta);
    }

    public TreeNode<Ruta> getRoot() {
        return root;
    }

    public void setRoot(TreeNode<Ruta> root) {
        this.root = root;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public DualListModel<Rol> getRolesSistema() {
        return rolesSistema;
    }

    public void setRolesSistema(DualListModel<Rol> rolesSistema) {
        this.rolesSistema = rolesSistema;
    }
}
