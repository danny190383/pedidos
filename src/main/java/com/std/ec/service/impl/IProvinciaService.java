package com.std.ec.service.impl;

import com.std.ec.entity.Provincia;
import java.util.List;
import java.util.Optional;

public interface IProvinciaService {
    List<Provincia> listar(Optional<String> estado);
}
