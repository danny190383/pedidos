package com.std.ec.repository;

import com.std.ec.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonaRepository  extends JpaRepository<Persona, Long>, JpaSpecificationExecutor<Persona> {

    @Query("SELECT e FROM Persona e WHERE " +
            "(:cedula IS NULL OR e.cedula = :cedula)")
    Persona buscarCedula(@Param("cedula") String cedula);
}
