package org.tasc.tasc_spring.order_service.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tasc.tasc_spring.api_common.model.response.OrderDto;
import org.tasc.tasc_spring.order_service.model.Order;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-21T20:03:00+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.1 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Override
    public Order toDto(OrderDto orderDto) {
        if ( orderDto == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        if ( orderDto.getOrderId() != null ) {
            order.orderId( UUID.fromString( orderDto.getOrderId() ) );
        }
        order.orderNo( orderDto.getOrderNo() );
        order.totalPrice( orderDto.getTotalPrice() );
        order.statusOrder( orderDto.getStatusOrder() );
        order.orderDetailsList( orderDetailsMapper.toListEntity( orderDto.getOrderDetailsList() ) );

        return order.build();
    }

    @Override
    public OrderDto toEntity(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        if ( order.getOrderId() != null ) {
            orderDto.setOrderId( order.getOrderId().toString() );
        }
        orderDto.setOrderNo( order.getOrderNo() );
        orderDto.setTotalPrice( order.getTotalPrice() );
        orderDto.setStatusOrder( order.getStatusOrder() );
        orderDto.setOrderDetailsList( orderDetailsMapper.toListDto( order.getOrderDetailsList() ) );

        return orderDto;
    }

    @Override
    public List<Order> toListEntity(List<OrderDto> d) {
        if ( d == null ) {
            return null;
        }

        List<Order> list = new ArrayList<Order>( d.size() );
        for ( OrderDto orderDto : d ) {
            list.add( toDto( orderDto ) );
        }

        return list;
    }

    @Override
    public List<OrderDto> toListDto(List<Order> e) {
        if ( e == null ) {
            return null;
        }

        List<OrderDto> list = new ArrayList<OrderDto>( e.size() );
        for ( Order order : e ) {
            list.add( toEntity( order ) );
        }

        return list;
    }

    @Override
    public OrderDto new_toEntity(Order order, String data) {
        if ( order == null && data == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        if ( order != null ) {
            if ( order.getOrderId() != null ) {
                orderDto.setOrderId( order.getOrderId().toString() );
            }
            orderDto.setOrderNo( order.getOrderNo() );
            orderDto.setTotalPrice( order.getTotalPrice() );
            orderDto.setStatusOrder( order.getStatusOrder() );
        }
        orderDto.setOrderDetailsList( null );

        return orderDto;
    }
}
