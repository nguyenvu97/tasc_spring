package org.tasc.tasc_spring.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tasc.tasc_spring.order_service.model.OrderDetails;

import java.util.UUID;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, UUID> {
}
