package org.tasc.tesc_spring.product_service.config;

import com.zaxxer.hikari.HikariDataSource;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;


import java.sql.Connection;

@Configuration
public class ConfigApp {
    @Value("${appConfig.default-page-size}")
    public  int pageSize;
    @Value("${appConfig.default-page-number}")
    public  int pageNumber;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public HikariDataSource hikariDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }


    @Bean
    public JdbcTemplate customJdbcTemplate(HikariDataSource hikariDataSource) {
        return new JdbcTemplate( hikariDataSource);  // Truyền datasource vào JdbcTemplate
    }
}
