package org.tasc.tesc_spring.product_service.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.repository.query.Param;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductDto extends Category{

    private String product_id;
    private String product_name;
    private String product_description;
    private double product_price;

    private int product_quantity;
    private String url;
    private double purchase_price;
    private String product_status;
//    private String category_name;
//    private String description;
    private String store_id;
}
