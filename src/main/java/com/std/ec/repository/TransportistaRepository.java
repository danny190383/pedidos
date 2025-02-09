package com.std.ec.repository;

import com.std.ec.entity.Transportista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransportistaRepository  extends JpaRepository<Transportista, Long>, JpaSpecificationExecutor<Transportista> {

    @Query("SELECT e FROM Transportista e WHERE e.estado = true")
    List<Transportista> listarActivas();
}
