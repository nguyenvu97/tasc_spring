package org.tasc.tesc_spring.product_service.rowmapper;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenericRowMapper<T>  implements RowMapper<T> {
    private final RowMapper<T> rowMapper;

    public GenericRowMapper(Class<T> type) {
        this.rowMapper = new BeanPropertyRowMapper<>(type);
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rowMapper.mapRow(rs, rowNum);
    }
}
