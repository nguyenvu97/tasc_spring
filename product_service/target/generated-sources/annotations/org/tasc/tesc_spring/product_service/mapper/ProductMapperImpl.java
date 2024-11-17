package org.tasc.tesc_spring.product_service.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.tasc.tesc_spring.product_service.dto.response.ProductDto;
import org.tasc.tesc_spring.product_service.model.Product;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-17T15:28:42+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.1 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toDto(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        if ( productDto.getProduct_id() != null ) {
            product.product_id( UUID.fromString( productDto.getProduct_id() ) );
        }
        product.product_name( productDto.getProduct_name() );
        product.product_description( productDto.getProduct_description() );
        product.product_price( productDto.getProduct_price() );
        product.product_quantity( productDto.getProduct_quantity() );
        product.url( productDto.getUrl() );
        product.purchase_price( productDto.getPurchase_price() );
        product.product_status( productDto.getProduct_status() );
        product.store_id( productDto.getStore_id() );

        return product.build();
    }

    @Override
    public ProductDto toEntity(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto.ProductDtoBuilder<?, ?> productDto = ProductDto.builder();

        if ( product.getProduct_id() != null ) {
            productDto.product_id( product.getProduct_id().toString() );
        }
        productDto.product_name( product.getProduct_name() );
        productDto.product_description( product.getProduct_description() );
        productDto.product_price( product.getProduct_price() );
        productDto.product_quantity( product.getProduct_quantity() );
        productDto.url( product.getUrl() );
        productDto.purchase_price( product.getPurchase_price() );
        productDto.product_status( product.getProduct_status() );
        productDto.store_id( product.getStore_id() );

        return productDto.build();
    }

    @Override
    public List<Product> toListEntity(List<ProductDto> d) {
        if ( d == null ) {
            return null;
        }

        List<Product> list = new ArrayList<Product>( d.size() );
        for ( ProductDto productDto : d ) {
            list.add( toDto( productDto ) );
        }

        return list;
    }

    @Override
    public List<ProductDto> toListDto(List<Product> e) {
        if ( e == null ) {
            return null;
        }

        List<ProductDto> list = new ArrayList<ProductDto>( e.size() );
        for ( Product product : e ) {
            list.add( toEntity( product ) );
        }

        return list;
    }
}
