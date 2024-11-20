package org.tasc.tasc_spring.api_common.model.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductDto  implements Serializable {
    private String product_id;
    private String product_name;
    private String product_description;
    private double product_price;
    private int product_quantity;
    private String url;
    private double purchase_price;
    private String product_status;
    private String category_name;
    private String description;
    private String store_id;
}
