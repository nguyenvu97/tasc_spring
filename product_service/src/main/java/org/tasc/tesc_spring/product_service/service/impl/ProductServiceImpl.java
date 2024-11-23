package org.tasc.tesc_spring.product_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerWebExchange;
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

import static org.tasc.tasc_spring.api_common.javaUtils.DecodeToken.get_customer;
import static org.tasc.tasc_spring.api_common.javaUtils.DecodeToken.performAction;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;
    private final ConfigApp configApp;
    private final CloudinaryService cloudinaryService;
    private final UserApi userApi;
    private final ObjectMapper objectMapper;


    @Override
    public ResponseData selectProduct(PageDto pageDto,String token) {

            if (pageDto.getPageNo() <0 || pageDto.getPageSize() <= 0) {
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
    public ResponseData findByProductId(List<ProductRequest> productRequests,String token , HttpServletRequest request) {
        String data = request.getHeader("role");
        if (data == null || "USER".equals(data)) {
            return ResponseData.builder()
                    .message("Unauthorized: insufficient permissions.")
                    .status_code(401)
                    .data("bad")
                    .build();
        }

        List<ProductDto> availableProducts = new ArrayList<>();
        for (ProductRequest productRequest : productRequests) {
            ProductDto productDto = productDao.selectProductById(productRequest.getProductId());

            if (ProductStatus.OPEN.toString().equals(productDto.getProduct_status()) && productDto.getProduct_quantity() >= productRequest.getQuantity() && productDto.getProduct_quantity() >0) {
                availableProducts.add(productDto);
            }
        }
        if (availableProducts.isEmpty() ) {
            return ResponseData.builder()
                    .status_code(404)
                    .message("No products found or all products are out of stock")
                    .build();
        }


        return ResponseData.builder()
                .status_code(200)
                .message("Products")
                .data(availableProducts)
                .build();
    }

    @Override
    public ResponseData updateProduct(List<ProductRequest> productRequests, String token,HttpServletRequest request) {
        String data = request.getHeader("role");

        if (data == null || "USER".equals(data)) {
            return ResponseData.builder()
                    .message("Unauthorized: insufficient permissions.")
                    .status_code(401)
                    .data("bad")
                    .build();
        }

        for (ProductRequest productRequest : productRequests) {
            ProductDto product = productDao.selectProductById(productRequest.getProductId());

            if (product == null) {
                return ResponseData.builder()
                        .message("Product not found with ID: " + productRequest.getProductId())
                        .status_code(404)
                        .data("Product not found")
                        .build();
            }
            if (productRequest.getQuantity() < 0) {
                return ResponseData.builder()
                        .message("Invalid quantity for product ID: " + productRequest.getProductId())
                        .status_code(400)
                        .data("bad request")
                        .build();
            }

            int newQuantity = product.getProduct_quantity() - productRequest.getQuantity();
            if (newQuantity < 0) {
                return ResponseData.builder()
                        .message("Insufficient stock for product ID: " + productRequest.getProductId())
                        .status_code(400)
                        .data("insufficient stock")
                        .build();
            }


            productDao.updateProduct(productRequest.getProductId(), newQuantity);
        }

        return ResponseData.builder()
                .message("Products updated successfully.")
                .status_code(200)
                .data("OK")
                .build();
    }


}
