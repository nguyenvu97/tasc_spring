package org.tasc.tesc_spring.product_service.service;

import org.springframework.web.multipart.MultipartFile;
import org.tasc.tasc_spring.api_common.model.request.ProductRequest;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tesc_spring.product_service.dto.request.PageDto;

import java.util.List;

public interface ProductService  {

    ResponseData selectProduct(PageDto pageDto,String token);
    ResponseData insertProduct(String product, List<MultipartFile> fileList,String token);
    ResponseData deleteProduct(String id);
    ResponseData selectProductById(String id);
    ResponseData findByProductId(List<ProductRequest>productId);
    ResponseData updateProduct(List<ProductRequest>productRequests);


}
