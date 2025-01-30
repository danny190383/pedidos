package com.std.ec.service;

import com.std.ec.entity.Rol;
import com.std.ec.entity.Ruta;
import com.std.ec.repository.RutaRepository;
import com.std.ec.service.impl.IRutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RutaService implements IRutaService {

    @Autowired
    private RutaRepository rutaRepository;

    @Override
    public List<Ruta> listar() {
        return (List<Ruta>) rutaRepository.findAll();
    }

    @Override
    public List<Ruta> listarPorNivel(Optional<Integer> nivel) {
        return rutaRepository.listarPorNivel (nivel);
    }

    @Override
    public List<Ruta> listarPorPadre(Optional<Long> padre) {
        return rutaRepository.listarPorPadre (padre);
    }

    @Override
    public List<Ruta> listarPorNivelEStado(Integer nivel, Boolean estado){
        return rutaRepository.listarPorNivelEstado(nivel,estado);
    }

    @Override
    public List<Ruta> listarPorPadreEStado(Long padre, Boolean estado){
        return rutaRepository.listarPorPadreEstado(padre,estado);
    }

    @Override
    public Map<String, List<String>> getRutaRolMappings(){
        Map<String, List<String>> map = new HashMap<>();
        List<Ruta> rutas = listar();
        for (Ruta ruta : rutas) {
            if (ruta.getRoles() != null && !ruta.getRoles().isEmpty()){
                List<String> roles = ruta.getRoles().stream()
                        .map(Rol::getNombre)
                        .collect(Collectors.toList());
                map.put(ruta.getRutaUrl(), roles);
            }
        }
        return map;
    }

    @Override
    public void eliminarRuta(Ruta ruta){
        rutaRepository.delete(ruta);
    }

    @Override
    public void guardarRuta(Ruta ruta){
        rutaRepository.save(ruta);
    }
}
