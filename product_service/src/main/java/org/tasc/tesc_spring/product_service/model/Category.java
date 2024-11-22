package org.tasc.tesc_spring.product_service.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    private int category_id;
    private String category_name;
    private String description;
}
