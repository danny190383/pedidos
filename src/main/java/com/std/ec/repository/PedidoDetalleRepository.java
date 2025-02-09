package com.std.ec.repository;

import com.std.ec.entity.EstacionServicio;
import com.std.ec.entity.PedidoDetalle;
import com.std.ec.entity.Terminal;
import com.std.ec.entity.TipoCombustible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Long> {
	@Query("SELECT COALESCE(SUM(pd.volumen), 0) FROM PedidoDetalle pd " +
		       "JOIN pd.pedido p " +
		       "WHERE p.estacionServicio = :estacionServicio " +
		       "AND p.terminal = :terminal " +
		       "AND pd.tipoCombustible = :tipoCombustible " +
		       "AND EXTRACT(YEAR FROM p.fechaRegistro) = :anio " +
		       "AND EXTRACT(MONTH FROM p.fechaRegistro) = :mes " +
		       "AND EXISTS (SELECT 1 FROM PedidoEstado pe WHERE pe.pedido = p AND pe.estadoPedido.idEstadoPedido = :estado)")
		BigDecimal sumarVolumenDespachadoPorEstacionTerminalYCombustible(
		        @Param("estacionServicio") EstacionServicio estacionServicio,
		        @Param("terminal") Terminal terminal,
		        @Param("tipoCombustible") TipoCombustible tipoCombustible,
		        @Param("anio") int anio,
		        @Param("mes") int mes,
		        @Param("estado") Long estado);
}
