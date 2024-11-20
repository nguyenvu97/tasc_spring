package org.tasc.tasc_spring.redis_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.model.Otp;
import org.tasc.tasc_spring.api_common.model.request.Cart;
import org.tasc.tasc_spring.redis_service.service.OtpService;

import java.util.List;

@RestController
@RequestMapping("api/v1/redis/otp")
@RequiredArgsConstructor
public class RedisOtpController {
    private final OtpService otpService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam(value = "key") String key, @RequestParam(value = "user_id") String user_id, @RequestBody Otp value, @RequestParam(required = false,value = "time") long timeout) {
        return ResponseEntity.ok().body(otpService.saveInRedis(key, user_id,value,timeout));

    }
    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam(value = "key") String key,@RequestParam(value = "user_id")String user_id) {
        return ResponseEntity.ok().body( otpService.getInRedis(key,user_id));

    }
    @GetMapping("delete")
    public ResponseEntity<?> delete( @RequestParam("user_id") String user_id, @RequestParam(value = "key") String key) {
        return ResponseEntity.ok().body(otpService.deleteProduct( key, user_id));
    }
}
