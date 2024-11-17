package org.tasc.tesc_spring.product_service.dao;

import org.tasc.tesc_spring.product_service.dto.request.PageDto;
import org.tasc.tesc_spring.product_service.dto.response.ProductDto;
import org.tasc.tesc_spring.product_service.model.Product;

import java.util.List;

public interface ProductDao {
    List<ProductDto> selectProduct(PageDto pageDto, String token );
    int insertProduct(Product  product,String storeId);
    ProductDto deleteProduct(String id);
    ProductDto selectProductById(String id);
}
