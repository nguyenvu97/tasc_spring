package org.tasc.tasc_spring.redis_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.model.request.Cart;
import org.tasc.tasc_spring.redis_service.service.TokenRedisService;

@RestController
@RequestMapping("api/v1/redis/token")
@RequiredArgsConstructor
public class RedisTokenController {
    private final TokenRedisService tokenRedisService;

    @PostMapping("/save")
    public ResponseEntity<?> save (@RequestParam(value = "key") String key, @RequestParam(value = "user_id") String user_id, @RequestParam(value = "token") String value, @RequestParam(required = false,value = "time") long timeout){
        return  ResponseEntity.ok().body(tokenRedisService.saveInRedis(key,user_id,value,timeout));
    }
    @PostMapping("/get")
    public ResponseEntity<?> get (@RequestParam(value = "key") String key, @RequestParam(value = "user_id") String user_id){
        return  ResponseEntity.ok().body(tokenRedisService.getInRedis(key,user_id));
    }
    @PostMapping("/delete")
    public ResponseEntity<?> delete (@RequestParam(value = "key") String key, @RequestParam(value = "user_id") String user_id){
        return  ResponseEntity.ok().body(tokenRedisService.deleteInRedis(key,user_id));
    }
}
