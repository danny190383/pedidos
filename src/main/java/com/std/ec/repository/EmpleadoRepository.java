package com.std.ec.repository;

import com.std.ec.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long>, JpaSpecificationExecutor<Empleado> {

    @Query("SELECT e FROM Empleado e WHERE " +
            "(:cedula IS NULL OR e.persona.cedula = :cedula)")
    Empleado buscarCedula(@Param("cedula") String cedula);
}