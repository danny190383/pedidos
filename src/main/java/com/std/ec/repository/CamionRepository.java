package com.std.ec.repository;

import com.std.ec.entity.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CamionRepository extends JpaRepository<Camion, Long>, JpaSpecificationExecutor<Camion> {
}
