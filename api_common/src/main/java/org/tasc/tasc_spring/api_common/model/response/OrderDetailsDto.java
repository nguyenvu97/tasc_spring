package org.tasc.tasc_spring.api_common.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDto {
    private String order_details_id;
    private String productId;
    private double price;
    private String productName;
    private int quantity;
    private String storeId;
    public String img;
}
