package org.tasc.tasc_spring.order_service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.tasc.tasc_spring.api_common.model.request.Cart;
import org.tasc.tasc_spring.api_common.model.response.ProductDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-23T22:42:26+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.1 (Oracle Corporation)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public Cart toDto(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Cart.CartBuilder cart = Cart.builder();

        cart.product_id( productDto.getProduct_id() );
        cart.product_name( productDto.getProduct_name() );
        cart.product_description( productDto.getProduct_description() );
        cart.product_price( productDto.getProduct_price() );
        cart.product_quantity( productDto.getProduct_quantity() );
        cart.url( productDto.getUrl() );
        cart.purchase_price( productDto.getPurchase_price() );
        cart.product_status( productDto.getProduct_status() );
        cart.category_name( productDto.getCategory_name() );
        cart.description( productDto.getDescription() );
        cart.store_id( productDto.getStore_id() );

        return cart.build();
    }

    @Override
    public ProductDto toEntity(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        ProductDto.ProductDtoBuilder productDto = ProductDto.builder();

        productDto.product_id( cart.getProduct_id() );
        productDto.product_name( cart.getProduct_name() );
        productDto.product_description( cart.getProduct_description() );
        productDto.product_price( cart.getProduct_price() );
        productDto.product_quantity( cart.getProduct_quantity() );
        productDto.url( cart.getUrl() );
        productDto.purchase_price( cart.getPurchase_price() );
        productDto.product_status( cart.getProduct_status() );
        productDto.category_name( cart.getCategory_name() );
        productDto.description( cart.getDescription() );
        productDto.store_id( cart.getStore_id() );

        return productDto.build();
    }

    @Override
    public List<Cart> toListEntity(List<ProductDto> d) {
        if ( d == null ) {
            return null;
        }

        List<Cart> list = new ArrayList<Cart>( d.size() );
        for ( ProductDto productDto : d ) {
            list.add( toDto( productDto ) );
        }

        return list;
    }

    @Override
    public List<ProductDto> toListDto(List<Cart> e) {
        if ( e == null ) {
            return null;
        }

        List<ProductDto> list = new ArrayList<ProductDto>( e.size() );
        for ( Cart cart : e ) {
            list.add( toEntity( cart ) );
        }

        return list;
    }

    @Override
    public Cart toDto(ProductDto productDto, String user_id) {
        if ( productDto == null && user_id == null ) {
            return null;
        }

        Cart.CartBuilder cart = Cart.builder();

        if ( productDto != null ) {
            cart.product_id( productDto.getProduct_id() );
            cart.product_name( productDto.getProduct_name() );
            cart.product_description( productDto.getProduct_description() );
            cart.product_price( productDto.getProduct_price() );
            cart.product_quantity( productDto.getProduct_quantity() );
            cart.url( productDto.getUrl() );
            cart.purchase_price( productDto.getPurchase_price() );
            cart.product_status( productDto.getProduct_status() );
            cart.category_name( productDto.getCategory_name() );
            cart.description( productDto.getDescription() );
            cart.store_id( productDto.getStore_id() );
        }
        cart.user_id( user_id );

        return cart.build();
    }
}
