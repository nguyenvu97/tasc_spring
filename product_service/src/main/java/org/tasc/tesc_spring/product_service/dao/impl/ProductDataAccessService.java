package org.tasc.tesc_spring.product_service.dao.impl;

import lombok.RequiredArgsConstructor;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tesc_spring.product_service.dao.ProductDao;
import org.tasc.tesc_spring.product_service.dto.request.PageDto;
import org.tasc.tesc_spring.product_service.dto.response.ProductDto;
import org.tasc.tesc_spring.product_service.model.Product;
import org.tasc.tesc_spring.product_service.model.ProductStatus;
import org.tasc.tesc_spring.product_service.rowmapper.GenericRowMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductDataAccessService implements ProductDao {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<ProductDto>  selectProduct(PageDto pageDto,String token) {

        int offset = pageDto.getPageNo() * pageDto.getPageSize();

        StringBuilder sql = new StringBuilder ("SELECT p.product_id, " +
                "p.product_name, " +
                "p.product_price, " +
                "p.product_quantity, " +
                "p.url, " +
                "p.product_description, " +
                "p.store_id, " +
                "p.purchase_price, " +
                "p.product_status, " +
                "c.category_name, " +
                "c.description " +
                "FROM product AS p " +
                "LEFT JOIN category AS c ON p.category_id = c.category_id");
        List<Object> params = new ArrayList<>();

        if (pageDto.getProductName() != null && !pageDto.getProductName().isEmpty()) {
            sql.append(" WHERE p.product_name LIKE ?");
            params.add("%" + pageDto.getProductName() + "%");
        }

        if (pageDto.getCategory() != null && !pageDto.getCategory().isEmpty()) {
            if (params.size() > 0) {
                sql.append(" AND c.category_name = ?");
            } else {
                sql.append(" WHERE c.category_name = ?");
            }
            params.add(pageDto.getCategory());
        }
        if (token != null) {
            sql.append(" AND p.store_id = ? ");
            params.add(token);
        }
        sql.append(" ORDER BY ").append(pageDto.getSortBy());

        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageDto.getPageSize());
        params.add(offset);
        RowMapper<ProductDto>rowMapper = new GenericRowMapper<>(ProductDto.class);
        return jdbcTemplate.query(sql.toString(), params.toArray(),rowMapper);

    }

    @Override
    public int insertProduct(Product product,String storeId) {
        String product_status = ProductStatus.OPEN.toString();
        LocalDateTime dateTime = LocalDateTime.now();

        String sql =  "INSERT INTO product (product_id, product_name, product_description, product_price, product_quantity,url, purchase_price,product_status,created_at,updated_at,category_id,store_id) VALUES (UUID(), ?, ?, ?, ?,  ?, ?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                product.getProduct_name(),
                product.getProduct_description(),
                product.getProduct_price(),
                product.getProduct_quantity(),
                product.getUrl(),
                product.getPurchase_price(),
                product_status,
                dateTime.toString(),
                dateTime.toString(),
                product.getCategory_id(),
                storeId
        );


    }

    @Override
    public ProductDto deleteProduct(String id) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        RowMapper<ProductDto> rowMapper = new GenericRowMapper<>(ProductDto.class);
        ProductDto product = jdbcTemplate.query(sql,rowMapper,id).stream().findFirst().get();
        product.setProduct_status(ProductStatus.CLOSED.toString());
        jdbcTemplate.update(sql,id);
       return product;
    }

    @Override
    public ProductDto selectProductById(String id)  {
        String sql =
                "SELECT p.product_id, " +
                "p.product_name, " +
                "p.product_price, " +
                "p.product_quantity, " +
                "p.url, " +
                "p.product_description, " +
                "p.purchase_price, " +
                "p.product_status, " +
                "c.category_name, " +
                "c.description,  " +
                "p.store_id " +
                "FROM product AS p " +
                "LEFT JOIN category AS c ON p.category_id = c.category_id " +
                "where p.product_id = ?";

        RowMapper<ProductDto> rowMapper = new GenericRowMapper<>(ProductDto.class);
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst().orElseThrow(() -> new EntityNotFound("not found",404));
    }

}
