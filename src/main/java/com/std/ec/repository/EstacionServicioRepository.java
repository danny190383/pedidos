package com.std.ec.repository;

import com.std.ec.entity.EstacionServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EstacionServicioRepository  extends JpaRepository<EstacionServicio, Long>, JpaSpecificationExecutor<EstacionServicio> {
    @Query("SELECT e FROM EstacionServicio e WHERE e.estado = true")
    List<EstacionServicio> listarActivas();
}
