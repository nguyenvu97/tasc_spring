package org.tasc.tasc_spring.order_service.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.request.AuthenticationRequest;
import org.tasc.tasc_spring.api_common.model.request.ProductRequest;
import org.tasc.tasc_spring.api_common.model.response.AuthenticationResponse;
import org.tasc.tasc_spring.api_common.model.response.CustomerDto;
import org.tasc.tasc_spring.api_common.model.response.ProductDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.api_common.product_api.ProductApi;
import org.tasc.tasc_spring.api_common.redis_api.RedisApi;
import org.tasc.tasc_spring.api_common.user_api.UserApi;
import org.tasc.tasc_spring.order_service.mapper.OrderMapper;
import org.tasc.tasc_spring.order_service.model.Order;
import org.tasc.tasc_spring.order_service.model.OrderDetails;
import org.tasc.tasc_spring.api_common.model.status.OrderStatus;
import org.tasc.tasc_spring.order_service.repository.OrderDetailsRepository;
import org.tasc.tasc_spring.order_service.repository.OrderRepository;
import org.tasc.tasc_spring.order_service.service.OrderDetailsService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.tasc.tasc_spring.api_common.config.RedisConfig.Cart_Key;
import static org.tasc.tasc_spring.api_common.javaUtils.DecodeToken.decodeToken;
import static org.tasc.tasc_spring.api_common.javaUtils.DecodeToken.get_customer;

@Service
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailsService {
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final UserApi userApi;
    private final ProductApi productApi;
    private final RedisApi redisApi;
    private final OrderMapper orderMapper;
    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;

    private String orderNo(){
        UUID uuid = UUID.randomUUID();
        return "orderNo"+uuid.toString();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseData createOrderDetails(List<ProductRequest> products, String token, boolean choose) {

        CustomerDto customerDto = get_customer(token, userApi, objectMapper);

        double total = 0.0;
        Order order;
       Map<String,Integer> map = products.stream().collect(Collectors.toMap(ProductRequest::getProductId, ProductRequest::getQuantity));
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        List<ProductDto> productDtos = checkProductQuantity(products);
        for (ProductDto productRequest : productDtos) {
            OrderDetails orderDetails = convert(productRequest);
                 Integer quantity  = map.get(productRequest.getProduct_id());
                orderDetails.setQuantity(quantity);
                total += (productRequest.getProduct_price() * quantity);

            orderDetailsList.add(orderDetails);
        }

        order = createOrder(total, orderNo(), customerDto.getId());

        order = orderRepository.save(order);
        for (OrderDetails orderDetails : orderDetailsList) {
            orderDetails.setOrder(order);
        }
        if (choose) {
            List<String> productIds = products.stream()
                    .map(ProductRequest::getProductId)
                    .collect(toList());
            redisApi.delete(productIds, customerDto.getId(), Cart_Key);
        }
        return ResponseData
                .builder()
                .message("Success")
                .data(orderDetailsRepository.saveAll(orderDetailsList))
                .build();
    }

    private List<ProductDto> checkProductQuantity(List<ProductRequest>products){

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(adminUsername, adminPassword);




            ResponseData responseData = productApi.find_product(products);
            if (responseData.status_code == 200 && responseData.data != null) {
                Map<String, List<ProductDto>> data = objectMapper.convertValue(responseData.data, new TypeReference<Map<String, List<ProductDto>>>() {
                });
                List<ProductDto> availableProducts = data.get("available");
                return availableProducts;


        }
        return null;

    }



    private OrderDetails convert( ProductDto productDto){
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setPrice(productDto.getProduct_price());
        orderDetails.setProductName(productDto.getProduct_name());
        orderDetails.setImg(productDto.getUrl());
        orderDetails.setStoreId(productDto.getStore_id());
        orderDetails.setProductId(productDto.getProduct_id());
        return orderDetails;
    }


    private Order createOrder(double total,String orderNo,String customerId){
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setTotalPrice(total);
        order.setStatusOrder(OrderStatus.PENDING);
        order.setOrderId(UUID.fromString(customerId));
        order.setCreate_at(LocalDateTime.now());
        order.setUpdate_at(LocalDateTime.now());
        return order;
    }
}
