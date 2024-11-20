package org.tasc.tasc_spring.redis_service.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.ex.InvalidCallException;
import org.tasc.tasc_spring.api_common.model.request.Cart;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.redis_service.service.RedisService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.tasc.tasc_spring.redis_service.javaUtils.Utils.get_data_redis;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl  implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private void deleteInRedis(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public ResponseData deleteProduct(List<String> product_ids, String key, String user_id) {

        List<Cart> carts = (List<Cart>) get_data_redis(key, user_id,redisTemplate);
        try{
            if (carts == null || carts.isEmpty()) {

                return ResponseData.builder()
                        .status_code(404)
                        .message("not found")
                        .data("")
                        .build();
            }


            List<Cart> updatedCarts = carts.stream()
                    .filter(c -> {
                        boolean toDelete = product_ids.contains(c.getProduct_id());
                        return !toDelete;
                    })
                    .collect(Collectors.toList());
            redisTemplate.opsForHash().put(key, user_id, updatedCarts);

            return ResponseData.builder()
                    .message("Product(s) deleted successfully")
                    .status_code(200)
                    .data("")
                    .build();

        }catch (InvalidCallException e){
            return ResponseData.builder()
                    .status_code(500)  // 500 Internal Server Error
                    .message("Error communicating with Redis")
                    .data("")  // Trả về data rỗng nếu không thể truy cập Redis
                    .build();
        }


    }

    @Override
    public ResponseData totalQuantity(String key, String user_id, String product_id, int quantity) {
        List<Cart> carts = (List<Cart>) get_data_redis(key, user_id,redisTemplate);
        if (carts == null || carts.isEmpty()) {
            throw new EntityNotFound("Cart not found", 404);
        }
        List<Cart> updatedCarts = carts.stream()
                .map(c -> {
                    if (c.getProduct_id().equals(product_id)) {
                        c.setProduct_quantity(quantity);
                    }
                    return c;
                })
                .collect(Collectors.toList());
        redisTemplate.opsForHash().put(key, user_id, updatedCarts);
        List<Cart> carts1 = (List<Cart>) get_data_redis(key, user_id,redisTemplate);
        if (carts1 == null || carts1.isEmpty()) {
            redisTemplate.opsForHash().delete(key, user_id);
        }
        return ResponseData.builder()
                .message("Product(s) updated successfully")
                .status_code(200)
                .data("")
                .build();
    }



    @Override
    public ResponseData getInRedis(String key, String userId) {
        try{
            List<Cart> data = (List<Cart>) get_data_redis(key, userId,redisTemplate);
            if (data == null || data.isEmpty()) {
                return ResponseData.builder()
                        .status_code(404)
                        .message("not found")
                        .data("") // If data is not null, retrieve from Redis
                        .build();
            }
            return ResponseData.builder()
                    .status_code(200)
                    .message("Success")
                    .data(redisTemplate.opsForHash().get(key, userId)) // If data is not null, retrieve from Redis
                    .build();

        }catch (InvalidCallException e){
            return ResponseData.builder()
                    .status_code(500)  // 500 Internal Server Error
                    .message("Error communicating with Redis")
                    .data("")  // Trả về data rỗng nếu không thể truy cập Redis
                    .build();
        }
    }

    @Override
    public ResponseData saveInRedis(String key, String user_id , Cart value, long time) {
        List<Cart> data = (List<Cart>) get_data_redis(key, user_id,redisTemplate);
        try{
            if(data == null || data.isEmpty()) {
                List<Cart> carts = new ArrayList<>();
                carts.add(value);
                redisTemplate.opsForHash().put(key,user_id, carts);
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return ResponseData
                        .builder()
                        .status_code(200)
                        .message("Success")
                        .data("ok")
                        .build();

            }else {
                if(data.size() >20){
                    ResponseData
                            .builder()
                            .status_code(400)
                            .message("cart max size")
                            .data("ok")
                            .build();
                }
                boolean productExists = data.stream()
                        .peek(c -> {
                            if (c.getProduct_id().equals(value.getProduct_id())) {
                                c.setProduct_quantity(c.getProduct_quantity() + value.getProduct_quantity());
                            }
                        })
                        .anyMatch(c -> c.getProduct_id().equals(value.getProduct_id()));


                if (!productExists) {
                    data.add(value);
                }

                redisTemplate.opsForHash().put(key, user_id, data);

                return ResponseData.builder()
                        .status_code(200)
                        .message("Success")
                        .data("ok")
                        .build();

            }

        }catch (InvalidCallException e){
            return ResponseData.builder()
                    .status_code(500)  // 500 Internal Server Error
                    .message("Error communicating with Redis")
                    .data("")  // Trả về data rỗng nếu không thể truy cập Redis
                    .build();
        }

        
    }
//    private List<Cart> getAll(String key, String user_id){
//        var data = redisTemplate.opsForHash().get(key, user_id);
//        List<Cart> carts = (List<Cart>) data;
//
//        return carts;
//    }
}
