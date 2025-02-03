package com.std.ec.repository;

import com.std.ec.entity.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CamionRepository extends JpaRepository<Camion, Long>, JpaSpecificationExecutor<Camion> {

    @Query("SELECT c FROM Camion c WHERE c.estado = true AND c.capacidad >= :capacidadNecesitada")
    List<Camion> findCamionesDisponiblesPorCapacidad(@Param("capacidadNecesitada") float capacidadNecesitada);

}
