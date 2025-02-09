package com.std.ec.repository;

import com.std.ec.entity.Impuesto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpuestoRepository extends JpaRepository<Impuesto, Long> {
}
