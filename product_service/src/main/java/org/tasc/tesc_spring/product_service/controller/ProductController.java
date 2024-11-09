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
    public ResponseEntity<?>get_all(@RequestBody(required = false) PageDto pageDto) {

        return ResponseEntity.ok().body(productService.selectProduct(pageDto));
    }

    @GetMapping("/get")
    public ResponseEntity<?>get(@RequestParam String product_id){
        return ResponseEntity.ok().body(productService.selectProductById(product_id));
    }
    @PostMapping
    public ResponseEntity<?>add(@RequestParam String product,  @RequestParam(required = false,value = "image") List<MultipartFile> files){

        return ResponseEntity.ok().body(productService.insertProduct(product,files));
    }
    @GetMapping("upload")
    public ResponseEntity<byte[]> getImage(@RequestParam String imgName) {
        byte[] imageBytes = productService.uploadFilesImage(imgName);

        if (imageBytes != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Đảm bảo định dạng chính xác
                    .body(imageBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?>delete(@RequestParam String product_id){
        return ResponseEntity.ok().body(productService.deleteProduct(product_id));
    }
}
