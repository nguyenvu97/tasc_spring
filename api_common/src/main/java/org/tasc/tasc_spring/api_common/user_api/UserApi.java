package org.tasc.tasc_spring.api_common.user_api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.model.request.AuthenticationRequest;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;

@FeignClient(name = "customer",url = "http://localhost:8083/api/v1/user")
@Repository
public interface UserApi {

    @GetMapping("/decode")
    ResponseData decode (@RequestHeader(value = "Authorization") String token);

    @GetMapping("/findBy")
    ResponseData  findBy(@RequestParam(value = "email") String email);

    @PostMapping("/login")
    ResponseData  authenticate(@RequestBody AuthenticationRequest request);

}
