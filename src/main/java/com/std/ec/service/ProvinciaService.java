package com.std.ec.service;

import com.std.ec.entity.Provincia;
import com.std.ec.repository.ProvinciaRepository;
import com.std.ec.service.impl.IProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProvinciaService implements IProvinciaService {
    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Override
    public List<Provincia> listar(Optional<String> estado) {
        return provinciaRepository.listarProvincias(estado);
    }
}
