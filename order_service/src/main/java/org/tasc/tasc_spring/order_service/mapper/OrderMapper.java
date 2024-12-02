package org.tasc.tasc_spring.order_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.tasc.tasc_spring.api_common.mapper.MapperAll;
import org.tasc.tasc_spring.api_common.mapper.MapperConfig;
import org.tasc.tasc_spring.api_common.model.response.OrderDto;
import org.tasc.tasc_spring.order_service.model.Order;

import java.util.List;

@Mapper(config = MapperConfig.class,uses = {OrderDetailsMapper.class})
public interface OrderMapper extends MapperAll<Order, OrderDto> {
    @Override
    Order toDto(OrderDto orderDto);

    @Override
    @Mappings({
            @Mapping(source = "order.orderDetailsList", target = "orderDetailsList"),
    })
    OrderDto toEntity(Order order);


    @Override
    List<Order> toListEntity(List<OrderDto> d);

    @Override
    List<OrderDto> toListDto(List<Order> e);

    @Mappings({
            @Mapping(target = "orderDetailsList", expression = "java(null)"),
    })
    OrderDto new_toEntity(Order order,String data);

}
