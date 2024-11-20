package org.tasc.tasc_spring.order_service.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.tasc.tasc_spring.api_common.model.response.OrderDetailsDto;
import org.tasc.tasc_spring.order_service.model.OrderDetails;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-20T09:06:14+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.1 (Oracle Corporation)"
)
@Component
public class OrderDetailsMapperImpl implements OrderDetailsMapper {

    @Override
    public OrderDetails toDto(OrderDetailsDto orderDetailsDto) {
        if ( orderDetailsDto == null ) {
            return null;
        }

        OrderDetails.OrderDetailsBuilder orderDetails = OrderDetails.builder();

        if ( orderDetailsDto.getOrder_details_id() != null ) {
            orderDetails.order_details_id( UUID.fromString( orderDetailsDto.getOrder_details_id() ) );
        }
        orderDetails.productId( orderDetailsDto.getProductId() );
        orderDetails.price( orderDetailsDto.getPrice() );
        orderDetails.productName( orderDetailsDto.getProductName() );
        orderDetails.quantity( orderDetailsDto.getQuantity() );
        orderDetails.storeId( orderDetailsDto.getStoreId() );
        orderDetails.img( orderDetailsDto.getImg() );

        return orderDetails.build();
    }

    @Override
    public OrderDetailsDto toEntity(OrderDetails orderDetails) {
        if ( orderDetails == null ) {
            return null;
        }

        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();

        if ( orderDetails.getOrder_details_id() != null ) {
            orderDetailsDto.setOrder_details_id( orderDetails.getOrder_details_id().toString() );
        }
        orderDetailsDto.setProductId( orderDetails.getProductId() );
        orderDetailsDto.setPrice( orderDetails.getPrice() );
        orderDetailsDto.setProductName( orderDetails.getProductName() );
        orderDetailsDto.setQuantity( orderDetails.getQuantity() );
        orderDetailsDto.setStoreId( orderDetails.getStoreId() );
        orderDetailsDto.setImg( orderDetails.getImg() );

        return orderDetailsDto;
    }

    @Override
    public List<OrderDetails> toListEntity(List<OrderDetailsDto> d) {
        if ( d == null ) {
            return null;
        }

        List<OrderDetails> list = new ArrayList<OrderDetails>( d.size() );
        for ( OrderDetailsDto orderDetailsDto : d ) {
            list.add( toDto( orderDetailsDto ) );
        }

        return list;
    }

    @Override
    public List<OrderDetailsDto> toListDto(List<OrderDetails> e) {
        if ( e == null ) {
            return null;
        }

        List<OrderDetailsDto> list = new ArrayList<OrderDetailsDto>( e.size() );
        for ( OrderDetails orderDetails : e ) {
            list.add( toEntity( orderDetails ) );
        }

        return list;
    }
}
