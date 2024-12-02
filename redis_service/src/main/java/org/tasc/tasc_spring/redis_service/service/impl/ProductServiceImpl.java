package org.tasc.tasc_spring.redis_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.model.response.ProductDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.redis_service.service.ProductService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.tasc.tasc_spring.api_common.config.RedisConfig.Product_Key_List;
import static org.tasc.tasc_spring.redis_service.javaUtils.Utils.check_key;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final RedisTemplate<String,Object> redisTemplate;
    @Override
    public ResponseData updateProduct(String key, List<ProductDto> productDtoList) {
        if (!check_key(key)) {
            return ResponseData
                    .builder()
                    .status_code(400)
                    .message("key is empty")
                    .data("")
                    .build();
        }
        for (ProductDto productDto : productDtoList) {
            redisTemplate.opsForHash().delete(key,productDto.getProduct_id());
        }
        Map<String,ProductDto> product = convets(productDtoList);
        redisTemplate.opsForHash().putAll(key,product);
        return ResponseData
                .builder()
                .status_code(200)
                .message("success")
                .data("")
                .build();
    }

    @Override
    public ResponseData deleteKey(String key) {
         redisTemplate.delete(key);
        if (!check_key(key)) {
            return ResponseData
                    .builder()
                    .status_code(400)
                    .message("key is empty")
                    .data("")
                    .build();
        }
        return ResponseData
                .builder()
                .data("")
                .status_code(200)
                .message("success")
                .build();
    }



    @Override
    public ResponseData saveAllProduct(String key, List<ProductDto> productDtoList) {
            Map<String,ProductDto> productDtoMap = convets(productDtoList);
            if (!check_key(key)) {
                return ResponseData
                        .builder()
                        .status_code(400)
                        .message("key is empty")
                        .data("")
                        .build();
            }
            redisTemplate.opsForHash().putAll(key, productDtoMap);
        List<String> productIds = productDtoList.stream()
                .map(ProductDto::getProduct_id)
                .collect(Collectors.toList());
        redisTemplate.opsForList().rightPushAll(Product_Key_List, productIds);
            return ResponseData
                    .builder()
                    .data(productDtoMap)
                    .message("Product updated successfully")
                    .status_code(200)
                    .build();
    }
    @Override
    public ResponseData getPagedProducts(String key, int page, int size) {
        if (!check_key(key)) {
            return ResponseData
                    .builder()
                    .status_code(400)
                    .message("key is empty")
                    .data("")
                    .build();
        }
        int start = (page - 1) * size;
        if (page <= 0 || size <= 0) {
            start = 0;
            size = 10;
        }
        int end = start + size;

        List<Object> productIds = redisTemplate.opsForList().range(Product_Key_List, start, end);

        if (productIds == null || productIds.isEmpty()) {
            return ResponseData
                    .builder()
                    .status_code(404)
                    .message("No products found for the given page")
                    .data("")
                    .build();
        }

        List<ProductDto> products = new ArrayList<>();
        for (Object productId : productIds) {
            ProductDto productDto = (ProductDto) redisTemplate.opsForHash().get(key, productId);
            if (productDto != null) {
                products.add(productDto);
            }
        }

        return ResponseData
                .builder()
                .status_code(200)
                .message("Products fetched successfully")
                .data(products)
                .build();
    }




    private Map<String,ProductDto> convets(List<ProductDto> productDtoList){
       return productDtoList.stream().collect(Collectors.toMap( ProductDto::getProduct_id, productDto -> productDto));
    }
}
