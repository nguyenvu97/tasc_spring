package org.tasc.tasc_spring.order_service.service;

import jakarta.servlet.http.HttpServletRequest;
import org.tasc.tasc_spring.api_common.model.response.CustomerDto;
import org.tasc.tasc_spring.api_common.model.response.OrderDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.order_service.dto.UserDto;

public interface OrderService {
    ResponseData findByOrderNo(String orderNo);
    ResponseData createOrder(String orderNo, UserDto userDto, String token, HttpServletRequest request );
    ResponseData update(int choose ,String orderNo);

}
