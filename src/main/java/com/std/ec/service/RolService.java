package com.std.ec.service;

import com.std.ec.entity.Rol;
import com.std.ec.repository.RolRepository;
import com.std.ec.service.impl.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService implements IRolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<Rol> listar() {
        return (List<Rol>) rolRepository.findAll();
    }

    @Override
    public void eliminarRol(Rol rol){
        rolRepository.delete(rol);
    }

    @Override
    public void guardarRol(Rol rol){
        rolRepository.save(rol);
    }
}
