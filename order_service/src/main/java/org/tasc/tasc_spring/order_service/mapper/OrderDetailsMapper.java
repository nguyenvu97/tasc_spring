package org.tasc.tasc_spring.order_service.mapper;

import org.mapstruct.Mapper;
import org.tasc.tasc_spring.api_common.mapper.MapperAll;
import org.tasc.tasc_spring.api_common.mapper.MapperConfig;
import org.tasc.tasc_spring.api_common.model.response.OrderDetailsDto;
import org.tasc.tasc_spring.order_service.model.OrderDetails;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface OrderDetailsMapper extends MapperAll<OrderDetails, OrderDetailsDto> {
    @Override
    OrderDetails toDto(OrderDetailsDto orderDetailsDto);

    @Override
    OrderDetailsDto toEntity(OrderDetails orderDetails);

    @Override
    List<OrderDetails> toListEntity(List<OrderDetailsDto> d);

    @Override
    List<OrderDetailsDto> toListDto(List<OrderDetails> e);
}
