package com.std.ec.repository;

import com.std.ec.entity.TerminalEstacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TerminalEstacionRepository extends JpaRepository<TerminalEstacion, Long> {
    @Query("SELECT t FROM TerminalEstacion t  ")
    List<TerminalEstacion> findAllWithRelations();

    @Query("SELECT es FROM TerminalEstacion es " +
            "LEFT JOIN FETCH es.estacionServicio e " +
            "WHERE e.estado = true AND es.terminal.idTerminal = :idTerminal")
    List<TerminalEstacion> listarTerminalEstacionesActivasPorTerminal(@Param("idTerminal") Long idTerminal);
}
