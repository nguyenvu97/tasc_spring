package org.tasc.tasc_spring.api_common.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.tasc.tasc_spring.api_common.model.status.OrderStatus;


import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class OrderDto implements Serializable {
    private String orderId;
    private String orderNo;
    private double totalPrice;
    private OrderStatus statusOrder;
    private List<OrderDetailsDto>orderDetailsList;
}
