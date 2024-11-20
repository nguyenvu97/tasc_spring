package org.tasc.tasc_spring.api_common.product_api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.tasc.tasc_spring.api_common.model.request.ProductRequest;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;

import java.util.List;

@FeignClient(name = "productApi",url = "http://localhost:1991/api/v1/product")
@Repository
public interface ProductApi {
    @PostMapping("/check")
    ResponseData find_product(@RequestBody List<ProductRequest> product_id);
    @PostMapping("/update")
    ResponseData update (@RequestBody List<ProductRequest> product);
}