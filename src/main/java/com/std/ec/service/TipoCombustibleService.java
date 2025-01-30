package com.std.ec.service;

import com.std.ec.entity.TipoCombustible;
import com.std.ec.repository.TipoCombustibleRepository;
import com.std.ec.service.impl.ITipoCombustibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TipoCombustibleService  implements ITipoCombustibleService {
    @Autowired
    private TipoCombustibleRepository tipoCombustibleRepository;

    private Map<Long, TipoCombustible> tipoCombustibleAsMap;

    @Override
    public List<TipoCombustible> listar() {
        return (List<TipoCombustible>) tipoCombustibleRepository.findAll();
    }

    @Override
    public void eliminarTipoCombustible(TipoCombustible tipoCombustible){
        tipoCombustibleRepository.delete(tipoCombustible);
    }

    @Override
    public void guardarTipoCombustible(TipoCombustible tipoCombustible){
        tipoCombustibleRepository.save(tipoCombustible);
    }

    @Override
    public Map<Long, TipoCombustible> getTipoCombustibleAsMap() {
        if (tipoCombustibleAsMap == null) {
            tipoCombustibleAsMap = this.listar().stream().collect(Collectors.toMap(TipoCombustible::getIdTipoCombustible, tipo -> tipo));
        }
        return tipoCombustibleAsMap;
    }
}
