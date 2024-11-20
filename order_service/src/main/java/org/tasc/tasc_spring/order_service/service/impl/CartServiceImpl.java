package org.tasc.tasc_spring.order_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.ex.InvalidCallException;
import org.tasc.tasc_spring.api_common.model.response.CustomerDto;
import org.tasc.tasc_spring.api_common.model.response.ProductDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.api_common.redis_api.RedisApi;
import org.tasc.tasc_spring.api_common.user_api.UserApi;
import org.tasc.tasc_spring.order_service.mapper.CartMapper;
import org.tasc.tasc_spring.api_common.model.request.Cart;
import org.tasc.tasc_spring.order_service.service.CartService;

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
    Cart cart1 = cartMapper.toDto(cart,customerDto.getId());
    ResponseData responseData =  redisApi.save(Cart_Key,customerDto.getId(),cart1,2592000);
    if (responseData.status_code ==200){
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

}
