package com.std.ec.service.impl;

import com.std.ec.entity.Canton;
import java.util.List;
import java.util.Optional;

public interface ICantonService {
    List<Canton> listarCantonesProvincia(Optional<String> estado, Optional<Long> idProvincia);
}
