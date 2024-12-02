package org.tasc.tasc_spring.redis_service.javaUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.tasc.tasc_spring.api_common.model.Otp;
import org.tasc.tasc_spring.api_common.model.request.Cart;

import java.util.List;
import java.util.Map;


public class Utils {
    public static Object get_data_redis(String key, String user_id, RedisTemplate<String, Object> redisTemplate){
        var  data  = redisTemplate.opsForHash().get(key, user_id);
        if (data instanceof String){
            return data;
        }else if (data instanceof List){
                return  data;
        }else if (data instanceof Map){
            return  data;
        }else if (data instanceof Otp){
            return  data;
        }else {
            return data;
        }
    }
    public static boolean check_key (String key){
        if (key == null || key.isEmpty()){
            return false;
        }
        return true;
    }
}
