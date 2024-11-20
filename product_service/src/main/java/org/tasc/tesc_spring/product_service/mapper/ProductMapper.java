package org.tasc.tesc_spring.product_service.mapper;

import org.mapstruct.Mapper;
import org.tasc.tasc_spring.api_common.mapper.MapperAll;
import org.tasc.tasc_spring.api_common.mapper.MapperConfig;
import org.tasc.tasc_spring.api_common.model.response.ProductDto;
import org.tasc.tesc_spring.product_service.model.Product;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface ProductMapper extends MapperAll<Product, ProductDto> {
    @Override
    Product toDto(ProductDto productDto);

    @Override
    ProductDto toEntity(Product product);

    @Override
    List<Product> toListEntity(List<ProductDto> d);

    @Override
    List<ProductDto> toListDto(List<Product> e);


}
