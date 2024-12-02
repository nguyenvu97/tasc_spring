package org.tasc.tasc_spring.order_service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.tasc.tasc_spring.api_common.mapper.MapperAll;
import org.tasc.tasc_spring.api_common.mapper.MapperConfig;
import org.tasc.tasc_spring.api_common.model.response.ProductDto;
import org.tasc.tasc_spring.api_common.model.request.Cart;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface CartMapper extends MapperAll<Cart, ProductDto> {
    @Override
    Cart toDto(ProductDto productDto);

    @Override
    ProductDto toEntity(Cart cart);

    @Override
    List<Cart> toListEntity(List<ProductDto> d);

    @Override
    List<ProductDto> toListDto(List<Cart> e);

//    @Mappings({
//            @Mapping(target = "user_id",expression = "java(user_id)")
//    })
//    Cart toDto(ProductDto productDto,String user_id);
}
