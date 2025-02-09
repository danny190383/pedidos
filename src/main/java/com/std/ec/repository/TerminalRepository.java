package com.std.ec.repository;

import com.std.ec.entity.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TerminalRepository extends JpaRepository<Terminal, Long> {

    @Query("SELECT t FROM Terminal t LEFT JOIN FETCH t.estacionServicioList es ")
    List<Terminal> findAllWithRelations();
}
