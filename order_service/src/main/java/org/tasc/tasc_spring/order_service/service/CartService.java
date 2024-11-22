package org.tasc.tasc_spring.order_service.service;

import org.tasc.tasc_spring.api_common.model.response.ProductDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;

import java.util.List;

public interface CartService {
    ResponseData addCart(ProductDto cart, String token);
    ResponseData getCart(String token);
    ResponseData deleteCart(List<String> product_id, String token);
    int countCart(String token);
    void sendNotification(String token);
}
