package com.std.ec.repository;

import com.std.ec.entity.Canton;
import com.std.ec.entity.Ruta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RutaRepository extends CrudRepository<Ruta, Long> {
    @Query("SELECT e FROM Ruta e WHERE (:nivel IS NULL OR e.nivel = :nivel)")
    List<Ruta> listarPorNivel(@Param("nivel") Optional<Integer> nivel);

    @Query("SELECT e FROM Ruta e WHERE (:padre IS NULL OR e.padre.idRuta = :padre)")
    List<Ruta> listarPorPadre(@Param("padre") Optional<Long> padre);

    @Query("SELECT e FROM Ruta e WHERE " +
            "(:nivel IS NULL OR e.nivel = :nivel) AND " +
            "(:estado IS NULL OR e.estado = :estado)")
    List<Ruta> listarPorNivelEstado(@Param("nivel") Integer nivel,
                                    @Param("estado") Boolean estado);

    @Query("SELECT e FROM Ruta e WHERE " +
            "(:padre IS NULL OR e.padre.idRuta = :padre) AND " +
            "(:estado IS NULL OR e.estado = :estado)")
    List<Ruta> listarPorPadreEstado(@Param("padre") Long padre,
                                    @Param("estado") Boolean estado);

}
