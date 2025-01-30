package com.std.ec.service.impl;

import com.std.ec.entity.Rol;
import java.util.List;

public interface IRolService {

    List<Rol> listar();

    void eliminarRol(Rol rol);

    void guardarRol(Rol rol);

}
