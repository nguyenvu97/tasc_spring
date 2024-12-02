package org.tasc.tasc_spring.redis_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.model.Otp;
import org.tasc.tasc_spring.api_common.model.response.ProductDto;
import org.tasc.tasc_spring.redis_service.service.OtpService;
import org.tasc.tasc_spring.redis_service.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/v1/redis/product")
@RequiredArgsConstructor
public class RedisProductController {
    private final ProductService productService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam(value = "key") String key, @RequestBody List<ProductDto> productDtoList) {
        return ResponseEntity.ok().body(productService.saveAllProduct(key,productDtoList));

    }
    @PostMapping("/update")
    public ResponseEntity<?> get(@RequestParam(value = "key") String key,@RequestBody List<ProductDto> productDtoList) {
        return ResponseEntity.ok().body( productService.updateProduct(key,productDtoList));

    }
    @GetMapping("delete")
    public ResponseEntity<?> delete( @RequestParam(value = "key") String key) {
        return ResponseEntity.ok().body(productService.deleteKey( key));
    }
    @GetMapping("get")
    public ResponseEntity<?> get(@RequestParam(value = "key") String key,@RequestParam(value = "page",required = false) int page, @RequestParam(value = "size",required = false)int size) {
        return ResponseEntity.ok().body(productService.getPagedProducts( key, page, size));
    }
}
