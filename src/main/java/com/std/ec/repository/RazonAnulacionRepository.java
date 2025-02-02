package com.std.ec.repository;

import com.std.ec.entity.RazonAnulacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RazonAnulacionRepository extends CrudRepository<RazonAnulacion, Long> {
    @Query("SELECT e FROM RazonAnulacion e WHERE e.estado = true AND e.tipo = :tipo")
    List<RazonAnulacion> listarActivasPorTipo(@Param("tipo") String tipo);
}
