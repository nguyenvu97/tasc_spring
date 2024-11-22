package org.tasc.tesc_spring.product_service.model;

import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private UUID product_id;
    private String product_name;
    private String product_description;
    private double product_price;
    private int product_quantity;
    private String url;
    private double purchase_price;
    private String product_status;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private int category_id;
    private String store_id;
    @PostConstruct
    public void init() {
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }

}

