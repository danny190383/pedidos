package com.std.ec.service;

import com.std.ec.entity.Canton;
import com.std.ec.repository.CantonRepository;
import com.std.ec.service.impl.ICantonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CantonService implements ICantonService {
    @Autowired
    private CantonRepository cantonRepository;

    @Override
    public  List<Canton> listarCantonesProvincia(Optional<String> estado, Optional<Long> idProvincia) {
        return cantonRepository.listarCantonesProvincia(estado, idProvincia);
    }
}
