package org.tasc.tasc_spring.order_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.response.OrderDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.api_common.model.status.OrderStatus;
import org.tasc.tasc_spring.order_service.mapper.OrderMapper;
import org.tasc.tasc_spring.order_service.model.Order;
import org.tasc.tasc_spring.order_service.repository.OrderRepository;
import org.tasc.tasc_spring.order_service.service.OrderService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;



    @Override
    public ResponseData findByOrderNo(String orderNo) {

       Order order =  orderRepository.getOrderByNo(orderNo);
       if (order == null) {
           throw new EntityNotFound("orderNo not found",404);
       }
        System.out.println(order);
        return ResponseData
                .builder()
                .message("ok")
                .data(orderMapper.new_toEntity(order,null))
                .status_code(200)
                .build();
    }

    @Override
    public ResponseData createOrder(int choose, String orderNo) {

        Order order = orderRepository.getOrderByNo(orderNo);
        if (order == null) {
            throw new EntityNotFound("orderNo not found", 404);
        }
        ResponseData.ResponseDataBuilder responseBuilder = ResponseData.builder().status_code(200);

        switch (choose) {
            case 1:
                order.setStatusOrder(OrderStatus.SUCCESS);
                order.setUpdate_at(LocalDateTime.now());
                responseBuilder.message("SUCCESS").data(orderMapper.toEntity(order));
                orderRepository.save(order);
                break;
            case 2:
                order.setStatusOrder(OrderStatus.FAILURE);
                order.setUpdate_at(LocalDateTime.now());
                responseBuilder.message("FAILURE").data(null);
                orderRepository.save(order);
                break;
            case 3:
                order.setStatusOrder(OrderStatus.CANCELLED);
                order.setUpdate_at(LocalDateTime.now());
                responseBuilder.message("CANCELLED").data(null);
                orderRepository.save(order);
                break;
            case 4:
                order.setStatusOrder(OrderStatus.ERROR);
                order.setUpdate_at(LocalDateTime.now());
                responseBuilder.message("ERROR").data(null);
                orderRepository.save(order);
                break;
            default:
                throw new IllegalArgumentException("Invalid choice for order status");
        }

        return responseBuilder.build();
    }
}
