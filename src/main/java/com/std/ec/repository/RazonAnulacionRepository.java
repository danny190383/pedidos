package com.std.ec.repository;

import com.std.ec.entity.RazonAnulacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RazonAnulacionRepository extends CrudRepository<RazonAnulacion, Long> {
    @Query("SELECT e FROM RazonAnulacion e WHERE e.estado = true")
    List<RazonAnulacion> listarActivas();
}
