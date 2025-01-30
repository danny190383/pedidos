package com.std.ec.repository;

import com.std.ec.entity.Canton;
import com.std.ec.entity.CantonPK;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CantonRepository extends CrudRepository<Canton, CantonPK> {

    @Query("SELECT e FROM Canton e WHERE " +
            "(:estado IS NULL OR e.estado = :estado) AND " +
            "(:idProvincia IS NULL OR e.cantonPK.idProvincia = :idProvincia)")
    List<Canton> listarCantonesProvincia(@Param("estado") Optional<String> estado,
                                         @Param("idProvincia") Optional<Long> idProvincia);
}
