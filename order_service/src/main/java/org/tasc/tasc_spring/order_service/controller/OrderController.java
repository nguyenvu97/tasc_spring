package org.tasc.tasc_spring.order_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.request.ProductRequest;
import org.tasc.tasc_spring.api_common.model.response.CustomerDto;
import org.tasc.tasc_spring.api_common.model.response.OrderDto;
import org.tasc.tasc_spring.order_service.dto.UserDto;
import org.tasc.tasc_spring.order_service.service.OrderDetailsService;
import org.tasc.tasc_spring.order_service.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailsService orderDetailsService;
    @PostMapping("/add")
    public ResponseEntity<?>createOrder(@RequestBody List<ProductRequest> products,@RequestHeader(value = "Authorization") String token,@RequestParam(value = "choose") boolean choose){
        try{
            return ResponseEntity.ok().body(orderDetailsService.createOrderDetails(products,token,choose));
        }catch (EntityNotFound e){
            throw e;
        }
    }
    @GetMapping
    public ResponseEntity<?>getOrderNo(@RequestParam(value = "orderNo") String orderNo){
        try{
            return ResponseEntity.ok().body(orderService.findByOrderNo(orderNo));
        }catch (EntityNotFound e){
            throw e;
        }

    }
    @PostMapping("update")
    public ResponseEntity<?>update(@RequestParam(value = "choose") int choose,@RequestParam(value = "orderNo") String orderNo){
        try{
            return ResponseEntity.ok().body(orderService.update(choose,orderNo));
        }catch (EntityNotFound e){
            throw e;
        }

    }
    @PostMapping("create")
    public ResponseEntity<?>create(@RequestHeader(value = "Authorization") String token, @RequestBody UserDto userDto, HttpServletRequest request,@RequestParam(value = "orderNo") String orderNo){
        try{
            return ResponseEntity.ok().body(orderService.createOrder(orderNo,userDto,token,request));
        }catch (EntityNotFound e){
            throw e;
        }
    }
}
