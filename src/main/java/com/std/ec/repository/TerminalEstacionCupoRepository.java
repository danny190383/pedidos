package com.std.ec.repository;

import com.std.ec.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminalEstacionCupoRepository extends JpaRepository<TerminalEstacionCupo, Long> {

    @Query("SELECT tec FROM TerminalEstacionCupo tec " +
            "JOIN tec.terminalEstacion te " +
            "WHERE te.terminal = :terminal " +
            "AND te.estacionServicio = :estacionServicio " +
            "AND tec.tipoCombustible = :tipoCombustible " +
            "AND tec.mes = :mesAnio")
    TerminalEstacionCupo findByTerminalAndEstacionServicioAndTipoCombustibleAndCupoMensual(
            @Param("terminal") Terminal terminal,
            @Param("estacionServicio") EstacionServicio estacionServicio,
            @Param("tipoCombustible") TipoCombustible tipoCombustible,
            @Param("mesAnio") String mesAnio);
}
