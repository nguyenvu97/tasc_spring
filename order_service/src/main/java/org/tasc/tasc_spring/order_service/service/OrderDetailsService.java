package org.tasc.tasc_spring.order_service.service;

import org.tasc.tasc_spring.api_common.model.request.ProductRequest;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;

import java.util.List;

public interface OrderDetailsService {
    ResponseData createOrderDetails(List<ProductRequest> products, String token, boolean choose);

}
