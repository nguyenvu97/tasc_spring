package org.tasc.tesc_spring.redis_service.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.model.ResponseData;
import org.tasc.tesc_spring.redis_service.service.RedisService;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl  implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public void deleteInRedis(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public ResponseData getInRedis(String key) {
        return ResponseData
                .builder()
                .status_code(200)
                .message("Success")
                .data(redisTemplate.opsForValue().get(key))
                .build();
    }

    @Override
    public ResponseData saveInRedis(String key, Object value,long time) {
            if (time >= 0){
                redisTemplate.opsForValue().set(key, value);
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return ResponseData
                        .builder()
                        .status_code(200)
                        .message("Success")
                        .data("ok")
                        .build();
            }
            else {
                redisTemplate.opsForValue().set(key, value);
                return ResponseData
                        .builder()
                        .status_code(200)
                        .message("Success")
                        .data("ok")
                        .build();
            }

    }
}
