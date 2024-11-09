package org.tasc.tesc_spring.product_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.tasc.tasc_spring.api_common.mapper.MapperAll;
import org.tasc.tasc_spring.api_common.mapper.MapperConfig;
import org.tasc.tesc_spring.product_service.dto.response.ProductDto;
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


//    @Mappings({
//            @Mapping(target = "category_name",expression = "java(category_name)"),
//            @Mapping(target = "category_description",expression = "java(description)")
//    })
//    ProductDto toEntity(Product product,String category_name,String description);


}
