package org.tasc.tasc_spring.payment_service.model;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.tasc.tasc_spring.api_common.model.status.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;
    private double amount;
    private String method;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private String orderNo;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

   @PostConstruct
    public void init() {
       createdAt = LocalDateTime.now();
       updatedAt = LocalDateTime.now();
   }


}
