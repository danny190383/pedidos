package com.std.ec.service.impl;

import com.std.ec.entity.TipoCombustible;
import java.util.List;
import java.util.Map;

public interface ITipoCombustibleService {
    List<TipoCombustible> listar();

    void eliminarTipoCombustible(TipoCombustible rol);

    void guardarTipoCombustible(TipoCombustible rol);

    Map<Long, TipoCombustible> getTipoCombustibleAsMap();
}
