package com.std.ec.service.impl;

import com.std.ec.entity.Ruta;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IRutaService {

    List<Ruta> listar();

    List<Ruta> listarPorNivel(Optional<Integer> nivel);

    List<Ruta> listarPorPadre(Optional<Long> padre);

    List<Ruta> listarPorNivelEStado(Integer nivel, Boolean estado);

    List<Ruta> listarPorPadreEStado(Long padre, Boolean estado);

    Map<String, List<String>> getRutaRolMappings();

    void eliminarRuta(Ruta ruta);

    void guardarRuta(Ruta ruta);
}
