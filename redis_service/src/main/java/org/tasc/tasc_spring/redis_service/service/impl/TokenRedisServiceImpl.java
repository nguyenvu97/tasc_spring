package org.tasc.tasc_spring.redis_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.redis_service.service.TokenRedisService;

import java.util.concurrent.TimeUnit;

import static org.tasc.tasc_spring.redis_service.javaUtils.Utils.get_data_redis;

@RequiredArgsConstructor
@Service
public class TokenRedisServiceImpl implements TokenRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public ResponseData saveInRedis(String key, String user_id, String value, long time) {
         redisTemplate.opsForHash().put(key,user_id,value);
         redisTemplate.expire(key, time, TimeUnit.SECONDS);
         return ResponseData
                 .builder()
                 .message("success")
                 .status_code(200)
                 .data("Ok")
                 .build();
    }

    @Override
    public ResponseData getInRedis(String key, String user_id) {
            String token = (String) get_data_redis(key, user_id,redisTemplate);
            if (token != null) {
                return ResponseData
                        .builder()
                        .message("ok")
                        .status_code(200)
                        .data(token)
                        .build();
            }else {
                return ResponseData
                        .builder()
                        .data("")
                        .status_code(404)
                        .message("not found")
                        .build();
            }

    }

    @Override
    public ResponseData deleteInRedis(String key, String user_id) {
        redisTemplate.opsForHash().delete(key,user_id);
        return ResponseData
                .builder()
                .message("success")
                .status_code(200)
                .data("Ok")
                .build();
    }


}
