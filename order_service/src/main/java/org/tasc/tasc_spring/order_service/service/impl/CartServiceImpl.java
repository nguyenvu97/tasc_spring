package org.tasc.tasc_spring.order_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.ex.InvalidCallException;
import org.tasc.tasc_spring.api_common.model.response.CustomerDto;
import org.tasc.tasc_spring.api_common.model.response.ProductDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.api_common.redis_api.RedisApi;
import org.tasc.tasc_spring.api_common.user_api.UserApi;
import org.tasc.tasc_spring.order_service.config.NotificationWebSocketHandler;
import org.tasc.tasc_spring.order_service.mapper.CartMapper;
import org.tasc.tasc_spring.api_common.model.request.Cart;
import org.tasc.tasc_spring.order_service.service.CartService;

import java.io.IOException;
import java.util.List;

import static org.tasc.tasc_spring.api_common.config.RedisConfig.Cart_Key;
import static org.tasc.tasc_spring.api_common.javaUtils.DecodeToken.get_customer;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
  private final RedisApi redisApi;
  private final ObjectMapper objectMapper;
  private  final UserApi userApi;
  private final CartMapper cartMapper;




  @Override
  public ResponseData addCart(ProductDto cart, String token) {
    CustomerDto customerDto = get_customer(token,userApi,objectMapper);
    Cart cart1 = cartMapper.toDto(cart);
    cart1.setProduct_quantity(1);
    ResponseData responseData =  redisApi.save(Cart_Key,customerDto.getId(),cart1,2592000);
    if (responseData.status_code ==200){
      sendNotification(token);
      return ResponseData
              .builder()
              .status_code(200)
              .message("ok")
              .data("ok")
              .build();
    }else if (responseData.status_code ==404){
      throw new EntityNotFound("get cart fail",404);
    }else {
      throw new InvalidCallException("get cart fail",500);
    }

  }
  @Override
  public ResponseData getCart( String token) {
    CustomerDto customerDto = get_customer(token,userApi,objectMapper);
      ResponseData responseData  = redisApi.get(Cart_Key,customerDto.getId());
      if (responseData.status_code == 200){
        return ResponseData
                .builder()
                .status_code(200)
                .message("ok")
                .data(responseData.data)
                .build();
      }else if (responseData.status_code ==404){
        throw new EntityNotFound("get cart fail",404);
      }else {
        throw new InvalidCallException("get cart fail",500);
      }
  }

  @Override
  public ResponseData deleteCart(List<String> product_id, String token ) {
    CustomerDto customerDto = get_customer(token,userApi,objectMapper);
    ResponseData responseData = redisApi.delete(product_id,customerDto.getId(),Cart_Key);
    if (responseData.status_code == 200){
      return ResponseData
              .builder()
              .data("")
              .message("ok")
              .status_code(200)
              .build();
    }
    else if (responseData.status_code ==404){
      throw new EntityNotFound("get cart fail",404);
    }else {
      throw new InvalidCallException("get cart fail",500);
    }

  }

  public int countCart(String token) {
    System.out.println(token);
    CustomerDto customerDto = get_customer(token, userApi, objectMapper);
    System.out.println(customerDto.getId());
    if (customerDto.getId() == null || customerDto.getId().isEmpty()) {
      throw new EntityNotFound("Invalid customer or token expired", 403);
    }
    ResponseData responseData = redisApi.count(Cart_Key, customerDto.getId());
    if (responseData.status_code == 200) {
      return (Integer) responseData.data;
    } else if (responseData.status_code ==404){
      return 0;
    }
    else {
      throw new InvalidCallException("Service error while fetching cart count", 500);
    }
  }
  public void sendNotification(String token)  {
    System.out.println("Token received: " + token);

    WebSocketSession session = NotificationWebSocketHandler.sessions.get(token);
    if (session != null) {
      System.out.println("Session is open: " + session.isOpen());
      if (session.isOpen()) {
        try {
          // Ví dụ lấy số lượng giỏ hàng từ cartService
          int count = countCart(token);
          System.out.println("Count from cartService: " + count);

          // Gửi thông báo đến client với số lượng giỏ hàng
          session.sendMessage(new TextMessage(String.valueOf(count)));
          System.out.println("Notification sent successfully.");
        } catch (IOException e) {
          System.err.println("Error sending notification: " + e.getMessage());
        }
      } else {
        System.err.println("Session is closed for token: " + token);
      }
    } else {
      System.err.println("Session not found for token: " + token);
    }
  }

}
