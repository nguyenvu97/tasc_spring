package org.tasc.tasc_spring.order_service.service;

import org.tasc.tasc_spring.api_common.model.response.OrderDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;

public interface OrderService {
    ResponseData findByOrderNo(String orderNo);
    ResponseData createOrder(int choose ,String orderNo);


}
