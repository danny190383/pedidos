package com.std.ec.repository;

import com.std.ec.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {

    @Query("SELECT MAX(CAST(p.codigo AS integer)) FROM Pedido p")
    Integer findMaxCodigo();

    @Query("SELECT p FROM Pedido p " +
            "WHERE FUNCTION('DATE', p.fechaRegistro) = :fecha " +
            "AND p.turnoPrioritario = true " +
            "AND p.terminal.idTerminal = :idTerminal")
    List<Pedido> findPedidosByFechaAndTurnoPrioritarioAndTerminal(
            @Param("fecha") LocalDate fecha,
            @Param("idTerminal") Long idTerminal
    );

}
