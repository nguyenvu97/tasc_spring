package org.tasc.tasc_spring.order_service.repository;


import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tasc.tasc_spring.api_common.model.response.OrderDto;
import org.tasc.tasc_spring.order_service.model.Order;

import java.util.UUID;

@Repository
public interface OrderRepository  extends JpaRepository<Order, UUID> {
    @Query(value = "CALL get_order(:orderNo)", nativeQuery = true)
    Order getOrderByNo(@Param("orderNo") String orderNo);
}
