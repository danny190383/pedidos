package com.std.ec.service;

import com.std.ec.entity.Usuario;
import com.std.ec.repository.UsuarioRepository;
import com.std.ec.service.impl.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> listar() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    public void guardarUsuario(Usuario usuario){
        usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findByUsername(String username){
        return usuarioRepository.findByUsername(username);
    }
}
