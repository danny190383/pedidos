package com.std.ec.repository;

import com.std.ec.entity.Provincia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProvinciaRepository extends CrudRepository<Provincia, Long> {
    @Query("SELECT e FROM Provincia e WHERE (:estado IS NULL OR e.estado = :estado)")
    List<Provincia> listarProvincias(@Param("estado") Optional<String> estado);
}
