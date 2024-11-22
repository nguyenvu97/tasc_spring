package org.tasc.tasc_spring.order_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.tasc.tasc_spring.api_common.model.response.OrderDto;
import org.tasc.tasc_spring.api_common.model.status.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID orderId;
    private String orderNo;
    private double totalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus statusOrder;
    private LocalDateTime create_at;
    private LocalDateTime update_at;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderDetails> orderDetailsList;
    @PostConstruct
    public void init() {
        create_at = LocalDateTime.now();
        update_at = LocalDateTime.now();
    }
}
