package com.std.ec.service.impl;

import com.std.ec.entity.Parroquia;
import java.util.List;
import java.util.Optional;

public interface IParroquiaService {
    List<Parroquia> listarParroquiasCanton(Optional<String> estado, Optional<Long> idProvincia, Optional<Long> idCanton);
}
