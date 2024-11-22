package org.tasc.tasc_spring.payment_service.reposittory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tasc.tasc_spring.api_common.model.status.OrderStatus;
import org.tasc.tasc_spring.payment_service.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByOrderNo(String orderNo);
    int countByOrderNo(String orderNo);
}
