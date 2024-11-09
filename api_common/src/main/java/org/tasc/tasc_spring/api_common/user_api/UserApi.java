package org.tasc.tasc_spring.api_common.user_api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.tasc.tasc_spring.api_common.model.ResponseData;

@FeignClient(name = "customer",url = "http://localhost:8083/api/v1/user")
@Repository
public interface UserApi {

    @GetMapping("/decode")
    ResponseData decode (@RequestHeader(value = "Authorization") String token);

}
