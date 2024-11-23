package org.tasc.tasc_spring.api_common.product_api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
//import org.tasc.tasc_spring.api_common.config.FeignConfig;
import org.tasc.tasc_spring.api_common.model.request.ProductRequest;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;

import java.util.List;

@FeignClient(name = "productApi",url = "http://localhost:1990/api/v1/product")
@Repository
public interface ProductApi {
    @PostMapping("/check")
    ResponseData find_product(@RequestBody List<ProductRequest> productRequests,@RequestHeader(value = "Authorization") String token);
    @PostMapping("/update")
    ResponseData update (@RequestBody List<ProductRequest> productRequests,
                         @RequestHeader(value = "Authorization") String token);
}
