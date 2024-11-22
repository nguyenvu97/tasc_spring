package org.tasc.tasc_spring.redis_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.model.request.Cart;
import org.tasc.tasc_spring.redis_service.service.RedisService;

import java.util.List;

@RestController
@RequestMapping("api/v1/redis")
@RequiredArgsConstructor
public class RedisCartController {
    private final RedisService redisService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam(value = "key") String key, @RequestParam(value = "user_id") String user_id, @RequestBody Cart value, @RequestParam(required = false,value = "time") long timeout) {
            return ResponseEntity.ok().body(redisService.saveInRedis(key, user_id,value,timeout));

    }
    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam(value = "key") String key,@RequestParam(value = "user_id")String user_id) {
            return ResponseEntity.ok().body( redisService.getInRedis(key,user_id));

    }
    @GetMapping
    public ResponseEntity<?> delete(@RequestParam(value = "product_id") List<String> product_id,@RequestParam("user_id") String user_id, @RequestParam(value = "key") String key) {
        return ResponseEntity.ok().body(redisService.deleteProduct(product_id, key, user_id));
    }
    @GetMapping("/quantity")
    public ResponseEntity<?> update_quantity(@RequestParam(value = "key") String key,@RequestParam(value = "user_id")String user_id,@RequestParam(value ="product_id") String product_id,@RequestParam(value = "quantity")int quantity) {

        return ResponseEntity.ok().body( redisService.getInRedis(key,user_id));
    }
    @GetMapping("count")
    public ResponseEntity<?> count(@RequestParam(value = "key") String key,@RequestParam(value = "user_id")String user_id) {
        return ResponseEntity.ok().body( redisService.countCart(key,user_id));
    }
}

