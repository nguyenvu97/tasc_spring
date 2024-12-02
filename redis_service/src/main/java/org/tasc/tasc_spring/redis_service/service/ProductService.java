package org.tasc.tasc_spring.redis_service.service;

import org.tasc.tasc_spring.api_common.model.response.ProductDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;

import java.util.List;

public interface ProductService {
    ResponseData saveAllProduct(String key,List<ProductDto>productDtoList);
    ResponseData updateProduct(String key,List<ProductDto> productDtoList);
    ResponseData deleteKey(String key);
    ResponseData getPagedProducts(String key, int page, int size);


}
