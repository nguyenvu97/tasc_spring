package org.tasc.tesc_spring.product_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor

@Data
public class ProductDto {
    private String product_id;
    private String product_name;
    private String product_description;
    private double product_price;
    private int product_quantity;
    private String img;
    private double purchase_price;
    private String product_status;
    private String category_name;
    private String description;
}
