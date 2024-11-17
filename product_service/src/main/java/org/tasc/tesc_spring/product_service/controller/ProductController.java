package org.tasc.tesc_spring.product_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    @PostMapping
    public ResponseEntity<?>add(@RequestParam(value = "product") String product,
                                @RequestParam(required = false,value = "image") List<MultipartFile> files,
                                @RequestHeader(value = "Authorization" )String token){

        return ResponseEntity.ok().body(productService.insertProduct(product,files,token));
    }

    @DeleteMapping
    public ResponseEntity<?>delete(@RequestParam String product_id){
        return ResponseEntity.ok().body(productService.deleteProduct(product_id));
    }
}
