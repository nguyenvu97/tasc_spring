package org.tasc.tasc_spring.api_common.order_api;//package org.tasc.api_common.order_api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.model.response.OrderDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;

@FeignClient(name = "Order",url = "http://localhost:2000/api/v1/order")
@Repository
public interface Order_Api {
    @GetMapping
    ResponseData getOrderNo(@RequestParam(value = "orderNo") String orderNo);
    @PostMapping
    ResponseData create(@RequestParam(value = "choose") int choose,@RequestParam(value = "orderNo") String orderNo);
}
