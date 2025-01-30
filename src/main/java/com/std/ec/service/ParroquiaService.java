package com.std.ec.service;

import com.std.ec.entity.Parroquia;
import com.std.ec.repository.ParroquiaRepository;
import com.std.ec.service.impl.IParroquiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ParroquiaService implements IParroquiaService {
    @Autowired
    private ParroquiaRepository parroquiaRepository;

    @Override
    public List<Parroquia> listarParroquiasCanton(Optional<String> estado, Optional<Long> idProvincia, Optional<Long> idCanton) {
        return parroquiaRepository.listarParroquiasCanton(estado, idProvincia, idCanton);
    }
}
