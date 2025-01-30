package com.std.ec.repository;

import com.std.ec.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PedidoRepository extends JpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {

    @Query("SELECT MAX(CAST(p.codigo AS integer)) FROM Pedido p")
    Integer findMaxCodigo();

}
