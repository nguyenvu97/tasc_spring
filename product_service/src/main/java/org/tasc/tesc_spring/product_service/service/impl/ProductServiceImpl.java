package org.tasc.tesc_spring.product_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.request.ProductRequest;
import org.tasc.tasc_spring.api_common.model.response.CustomerDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.api_common.model.status.ProductStatus;
import org.tasc.tasc_spring.api_common.user_api.UserApi;
import org.tasc.tesc_spring.product_service.config.ConfigApp;
import org.tasc.tesc_spring.product_service.dao.ProductDao;
import org.tasc.tesc_spring.product_service.dto.request.PageDto;
import org.tasc.tasc_spring.api_common.model.response.ProductDto;
import org.tasc.tesc_spring.product_service.model.Product;
import org.tasc.tesc_spring.product_service.service.CloudinaryService;
import org.tasc.tesc_spring.product_service.service.ProductService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;
    private final ConfigApp configApp;
    private final CloudinaryService cloudinaryService;
    private final UserApi userApi;


    @Value("${uploading.videoSaveFolder}")
    private String FOLDER_PATH;

    @Override
    public ResponseData selectProduct(PageDto pageDto,String token) {

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
        if (token == null || token.isEmpty()) {
            token = null;
        }
            return ResponseData
                    .builder()
                    .data(productDao.selectProduct(pageDto,token))
                    .status_code(200)
                    .message("ok")
                    .build();
    }


    private String randomNumber (){

        UUID uuid= UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public ResponseData insertProduct(String product,List<MultipartFile> fileList,String token)  {


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
            ResponseData responseData = userApi.decode(token);
            if (responseData.status_code != 200 || responseData.data == null) {
                throw  new EntityNotFound("token ex",401);
            }

                CustomerDto customerDto = null;
               if (responseData.data instanceof Map){
                   Map<String, Object> dataMap = (Map<String, Object>) responseData.data;
                   customerDto = objectMapper.convertValue(dataMap, CustomerDto.class);

                   int data =  productDao.insertProduct(product1,customerDto.getId());

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
        return null;



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

    @Override
    public ResponseData findByProductId(List<ProductRequest> productRequests) {
        List<ProductDto> availableProducts = new ArrayList<>();
        List<ProductDto> outOfStockProducts = new ArrayList<>();

        for (ProductRequest productRequest : productRequests) {
            ProductDto productDto = productDao.selectProductById(productRequest.getProductId());

            if (ProductStatus.OPEN.toString().equals(productDto.getProduct_status()) && productDto.getProduct_quantity() >= productRequest.getQuantity()) {
                availableProducts.add(productDto);
            } else {
                outOfStockProducts.add(productDto);
            }
        }

        if (availableProducts.isEmpty() && outOfStockProducts.isEmpty()) {
            return ResponseData.builder()
                    .status_code(404)
                    .message("No products found or all products are out of stock")
                    .build();
        }


        return ResponseData.builder()
                .status_code(200)
                .message("Products")
                .data(new HashMap<String, List<ProductDto>>() {{
                    put("available", availableProducts);
                    put("outOfStock", outOfStockProducts);
                }})
                .build();
    }

    @Override
    public ResponseData updateProduct(List<ProductRequest> productRequests) {
        productRequests.stream().forEach(productRequest -> {
          ProductDto product =  productDao.selectProductById(productRequest.getProductId());
             productDao.updateProduct(productRequest.getProductId(),product.getProduct_quantity() - productRequest.getQuantity());
        });
        return ResponseData
                .builder()
                .message("updateOk")
                .status_code(200)
                .data("Ok")
                .build();
    }


}
