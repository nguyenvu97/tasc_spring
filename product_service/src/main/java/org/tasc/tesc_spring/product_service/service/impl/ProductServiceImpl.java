package org.tasc.tesc_spring.product_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.ResponseData;
import org.tasc.tesc_spring.product_service.config.ConfigApp;
import org.tasc.tesc_spring.product_service.dao.ProductDao;
import org.tasc.tesc_spring.product_service.dto.request.PageDto;
import org.tasc.tesc_spring.product_service.dto.response.ProductDto;
import org.tasc.tesc_spring.product_service.model.Product;
import org.tasc.tesc_spring.product_service.service.CloudinaryService;
import org.tasc.tesc_spring.product_service.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;
    private final ConfigApp configApp;
    private final CloudinaryService cloudinaryService;

    @Value("${uploading.videoSaveFolder}")
    private String FOLDER_PATH;

    @Override
    public ResponseData selectProduct(PageDto pageDto) {

            if (pageDto.getPageNo() <=0 || pageDto.getPageSize() <= 0) {
                pageDto.setPageSize(configApp.pageSize);
                pageDto.setPageNo(configApp.pageNumber);
            }
        if (pageDto.getProductName() == null || pageDto.getProductName().isEmpty()) {
            pageDto.setProductName(null);
        }

        if (pageDto.getCategory() == null || pageDto.getCategory().isEmpty()) {
            pageDto.setCategory(null);
        }
        if (pageDto.getSortBy() == null || pageDto.getSortBy().isEmpty()) {
            pageDto.setSortBy("product_id");
        }
            return ResponseData
                    .builder()
                    .data(productDao.selectProduct(pageDto))
                    .status_code(200)
                    .message("ok")
                    .build();
    }


    private String randomNumber (){

        UUID uuid= UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public ResponseData insertProduct(String product,List<MultipartFile> fileList)  {
        if (fileList != null && !fileList.isEmpty()) {
            List<String> images = new ArrayList<>();
            for (MultipartFile file : fileList) {

                images.add(  cloudinaryService.uploadFile(file));
            }

            String joinedImages = String.join(",", images);
            ObjectMapper objectMapper = new ObjectMapper();
            Product product1 = null;
            try {
                 product1 = objectMapper.readValue(product, Product.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            product1.setUrl(joinedImages);
            int data =  productDao.insertProduct(product1);

            if (data == 0){
                throw new EntityNotFound("not add product",404);
            }
        }
        return ResponseData.builder()
                .status_code(200)
                .message("ok")
                .data("create_ok")
                .build();


    }

    @Override
    public ResponseData deleteProduct(String id) {
        ProductDto data =  productDao.deleteProduct(id);
        if (data == null){
            throw new EntityNotFound("not add product",404);
        }
        return ResponseData
                .builder()
                .status_code(200)
                .message("ok")
                .data(data)
                .build();
    }

    @Override
    public ResponseData selectProductById(String id) {
        ProductDto product = productDao.selectProductById(id);
        if (product == null) {
            throw new EntityNotFound("not found",404);
        }

        return ResponseData
                .builder()
                .status_code(200)
                .message("ok")
                .data(product)
                .build();
    }





}
