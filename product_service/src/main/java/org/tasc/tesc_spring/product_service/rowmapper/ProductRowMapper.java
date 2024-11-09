package org.tasc.tesc_spring.product_service.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.tasc.tesc_spring.product_service.dto.response.ProductDto;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<ProductDto> {
    @Override
    public ProductDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductDto(
                rs.getString("product_id"),
                rs.getString("product_name"),
                rs.getString("product_description"),
                rs.getDouble("product_price"),
                rs.getInt("product_quantity"),
                rs.getString("img"),
                rs.getDouble("purchase_price"),
                rs.getString("product_status"),
                rs.getString("category_name"),
                rs.getString("description")
        );
    }
}
