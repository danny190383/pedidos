package com.std.ec.repository;

import com.std.ec.entity.ImpuestoTarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImpuestoTarifaRepository extends JpaRepository<ImpuestoTarifa, Long> {
    @Query("SELECT it FROM ImpuestoTarifa it WHERE it.impuesto.idImpuesto = :idImpuesto AND it.estado = true")
    List<ImpuestoTarifa> findByImpuestoIdAndEstadoActivo(@Param("idImpuesto") Long idImpuesto);
}
