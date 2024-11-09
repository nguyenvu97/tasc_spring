package org.tasc.tesc_spring.product_service.dto.request;


import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
    private String sortBy;
    private String productName;
    private String category;
    private int pageNo;
    private int pageSize;

}
