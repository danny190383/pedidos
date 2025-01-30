package com.std.ec.service.impl;

import com.std.ec.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> listar();

    void guardarUsuario(Usuario usuario);

    Optional<Usuario> findByUsername(String username);
}
