package org.tasc.tesc_spring.product_service.model;

import lombok.*;

import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private UUID product_id;
    private String product_name;
    private String product_description;
    private double product_price;
    private int product_quantity;
    private String path;
    private String img;
    private double purchase_price;
    private String product_status;
    private String created_at;
    private String updated_at;
    private int category_id;

}

