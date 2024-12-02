package org.tasc.tasc_spring.redis_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.model.Otp;
import org.tasc.tasc_spring.api_common.model.request.Cart;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.redis_service.service.OtpService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.tasc.tasc_spring.redis_service.javaUtils.Utils.check_key;
import static org.tasc.tasc_spring.redis_service.javaUtils.Utils.get_data_redis;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final RedisTemplate<String,Object> redisTemplate;
    @Override
    public ResponseData saveInRedis(String key, String user_id, Otp value, long timeout) {
        redisTemplate.opsForHash().put(key,user_id,value);
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        return ResponseData
                .builder()
                .message("success")
                .status_code(200)
                .data("ok")
                .build();
    }

    @Override
    public ResponseData getInRedis(String key, String user_id) {
        if (!check_key(key)){
            ResponseData
                    .builder()
                    .message("key not exist")
                    .status_code(404)
                    .data("ok")
                    .build();
        }
        Otp otp = (Otp) get_data_redis(key, user_id,redisTemplate);
        return ResponseData
                .builder()
                .message("success")
                .status_code(200)
                .data(otp)
                .build();
    }

    @Override
    public ResponseData deleteProduct( String key, String user_id) {
        if (!check_key(key)){
            ResponseData
                    .builder()
                    .message("key not exist")
                    .status_code(404)
                    .data("ok")
                    .build();
        }
        redisTemplate.opsForHash().delete(key,user_id);
        return ResponseData
                .builder()
                .message("success")
                .status_code(200)
                .data("ok")
                .build();
    }
}
