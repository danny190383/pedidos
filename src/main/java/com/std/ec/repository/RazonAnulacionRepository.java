package com.std.ec.repository;

import com.std.ec.entity.RazonAnulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RazonAnulacionRepository extends JpaRepository<RazonAnulacion, Long> {
    @Query("SELECT e FROM RazonAnulacion e WHERE e.estado = true AND e.tipo = :tipo")
    List<RazonAnulacion> listarActivasPorTipo(@Param("tipo") String tipo);
}
