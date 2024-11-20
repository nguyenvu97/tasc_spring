package org.tasc.tasc_spring.redis_service.service;


import org.tasc.tasc_spring.api_common.model.request.Cart;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;

import java.util.List;

public interface RedisService {
    ResponseData saveInRedis(String key, String user_id, Cart value, long timeout);
    ResponseData getInRedis(String key,String user_id);
    ResponseData deleteProduct(List<String> product_id, String key, String user_id);
    ResponseData totalQuantity  (String key,String user_id,String product_id,int quantity);

}
