package org.tasc.tesc_spring.redis_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasc.tesc_spring.redis_service.service.RedisService;

@RestController
@RequestMapping("api/v1/redis")
@RequiredArgsConstructor
public class RedisController {
    private final RedisService redisService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam String key, @RequestParam String value,@RequestParam(required = false) long timeout) {
       return ResponseEntity.ok().body( redisService.saveInRedis(key, value,timeout));
    }
    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam String key) {
        return ResponseEntity.ok().body( redisService.getInRedis(key));
    }
    @GetMapping
    public void delete(@RequestParam String key) {
        redisService.deleteInRedis(key);
    }
}
