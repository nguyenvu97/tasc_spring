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
    date = "2024-11-09T20:29:16+0700",
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
        product.img( productDto.getImg() );
        product.purchase_price( productDto.getPurchase_price() );
        product.product_status( productDto.getProduct_status() );

        return product.build();
    }

    @Override
    public ProductDto toEntity(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        if ( product.getProduct_id() != null ) {
            productDto.setProduct_id( product.getProduct_id().toString() );
        }
        productDto.setProduct_name( product.getProduct_name() );
        productDto.setProduct_description( product.getProduct_description() );
        productDto.setProduct_price( product.getProduct_price() );
        productDto.setProduct_quantity( product.getProduct_quantity() );
        productDto.setImg( product.getImg() );
        productDto.setPurchase_price( product.getPurchase_price() );
        productDto.setProduct_status( product.getProduct_status() );

        return productDto;
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
