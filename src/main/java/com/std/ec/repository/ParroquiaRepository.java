package com.std.ec.repository;

import com.std.ec.entity.Parroquia;
import com.std.ec.entity.ParroquiaPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ParroquiaRepository extends JpaRepository<Parroquia, ParroquiaPK> {

    @Query("SELECT e FROM Parroquia e WHERE " +
            "(:estado IS NULL OR e.estado = :estado) AND " +
            "(:idProvincia IS NULL OR e.parroquiaPK.idProvincia = :idProvincia) AND " +
            "(:idCanton IS NULL OR e.parroquiaPK.idCanton = :idCanton)")
    List<Parroquia> listarParroquiasCanton(@Param("estado") Optional<String> estado,
                                           @Param("idProvincia") Optional<Long> idProvincia,
                                           @Param("idCanton") Optional<Long> idCanton);

}
