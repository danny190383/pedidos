package com.std.ec.repository;

import com.std.ec.entity.EstacionServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstacionServicioRepository  extends JpaRepository<EstacionServicio, Long>, JpaSpecificationExecutor<EstacionServicio> {
    @Query("SELECT e FROM EstacionServicio e LEFT JOIN FETCH e.terminalList es WHERE e.estado = true")
    List<EstacionServicio> listarActivas();

    @Query("SELECT e FROM EstacionServicio e LEFT JOIN FETCH e.terminalList es WHERE e.estado = true AND es.terminal.idTerminal = :idTerminal")
    List<EstacionServicio> listarActivasPorTerminal(@Param("idTerminal") Long idTerminal);
}
