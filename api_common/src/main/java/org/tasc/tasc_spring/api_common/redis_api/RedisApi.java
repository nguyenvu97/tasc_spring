package org.tasc.tasc_spring.api_common.redis_api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.tasc.tasc_spring.api_common.model.Otp;
import org.tasc.tasc_spring.api_common.model.request.Cart;
import org.tasc.tasc_spring.api_common.model.response.ProductDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;

import java.util.List;

@FeignClient(name = "redis",url = "http://localhost:1999/api/v1/redis")
@Repository
public interface RedisApi {
    // cart
    @PostMapping("/save")
    ResponseData save (@RequestParam(value = "key") String key, @RequestParam(value = "user_id") String user_id, @RequestBody Cart value, @RequestParam(required = false,value = "time") long timeout) ;
    @GetMapping("/get")
    ResponseData get(@RequestParam(value = "key") String key,@RequestParam(value = "user_id")String user_id);
    @GetMapping()
    ResponseData delete (@RequestParam(value = "product_id") List<String> product_id,@RequestParam("user_id") String user_id, @RequestParam(value = "key") String key);
    @GetMapping("/count")
    ResponseData count(@RequestParam(value = "key") String key,@RequestParam(value = "user_id")String user_id);

    // token
    @PostMapping("/token/save")
    ResponseData saveToken (@RequestParam(value = "key") String key, @RequestParam(value = "user_id") String user_id, @RequestParam(value = "token") String value, @RequestParam(required = false,value = "time") long timeout);
    @PostMapping("/token/get")
    ResponseData getToken (@RequestParam(value = "key") String key, @RequestParam(value = "user_id") String user_id);
    @PostMapping("/token/delete")
    ResponseData delete (@RequestParam(value = "key") String key, @RequestParam(value = "user_id") String user_id);

    // otp
    @PostMapping("/otp/save")
    ResponseData saveOtp (@RequestParam(value = "key") String key, @RequestParam(value = "user_id") String user_id, @RequestBody Otp value, @RequestParam(required = false,value = "time") long timeout) ;
    @GetMapping("/otp/get")
    ResponseData getOtp(@RequestParam(value = "key") String key,@RequestParam(value = "user_id")String user_id);
    @GetMapping("otp/delete")
    ResponseData deleteOtp ( @RequestParam(value = "key") String key,@RequestParam("user_id") String user_id);

    //product
    @PostMapping("product/save")
    ResponseData saveProduct(@RequestParam(value = "key") String key, @RequestBody List<ProductDto> productDtoList);
    @PostMapping("product/update")
    ResponseData updateProduct(@RequestParam(value = "key") String key,@RequestBody List<ProductDto> productDtoList);
    @GetMapping("product/delete")
    ResponseData deleteProduct( @RequestParam(value = "key") String key);
    @GetMapping("get")
    ResponseData getProduct(@RequestParam(value = "key") String key,@RequestParam(value = "page",required = false) int page, @RequestParam(value = "size",required = false)int size);




}
