package org.tasc.tesc_spring.product_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerWebExchange;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.request.ProductRequest;
import org.tasc.tesc_spring.product_service.dto.request.PageDto;
import org.tasc.tesc_spring.product_service.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    public final ProductService productService;

    @PostMapping("/get_all")
    public ResponseEntity<?>get_all(@RequestBody(required = false) PageDto pageDto,@RequestHeader(value = "Authorization",required = false)String token) {
        return ResponseEntity.ok().body(productService.selectProduct(pageDto,token));
    }

    @GetMapping("/get")
    public ResponseEntity<?>get(@RequestParam(value = "product_id") String product_id){
        return ResponseEntity.ok().body(productService.selectProductById(product_id));
    }
    @PostMapping("/check")
    public ResponseEntity<?>check_quantity(@RequestBody List<ProductRequest> product_id,@RequestHeader("Authorization")String token ,HttpServletRequest httpServletRequest){
        return ResponseEntity.ok().body(productService.findByProductId(product_id,token,httpServletRequest));
    }
    @PostMapping("/add")
    public ResponseEntity<?>add(@RequestParam(value = "product") String product,
                                @RequestParam(required = false,value = "image") List<MultipartFile> files,
                                @RequestHeader(value = "Authorization" )String token){

        return ResponseEntity.ok().body(productService.insertProduct(product,files,token));
    }

    @DeleteMapping
    public ResponseEntity<?>delete(@RequestParam String product_id){
        return ResponseEntity.ok().body(productService.deleteProduct(product_id));
    }
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody List<ProductRequest> productRequests,
                                    @RequestHeader(value = "Authorization") String token,HttpServletRequest request) {

        try{
            return ResponseEntity.ok().body(productService.updateProduct(productRequests, token, request));
        } catch (EntityNotFound e){
           throw e;
        }
    }
}
