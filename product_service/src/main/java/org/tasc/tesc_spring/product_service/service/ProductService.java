package org.tasc.tesc_spring.product_service.service;

import org.springframework.web.multipart.MultipartFile;
import org.tasc.tasc_spring.api_common.model.ResponseData;
import org.tasc.tesc_spring.product_service.dto.request.PageDto;

import java.util.List;

public interface ProductService  {

    ResponseData selectProduct(PageDto pageDto);
    ResponseData insertProduct(String product, List<MultipartFile> fileList) ;

    ResponseData deleteProduct(String id);
    ResponseData selectProductById(String id);

    byte[] uploadFilesImage(String fileName);

}
