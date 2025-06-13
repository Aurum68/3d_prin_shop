package org.practice._3d_prin_shop.util;

import org.mapstruct.Mapper;
import org.practice._3d_prin_shop.dto.ProductDto;
import org.practice._3d_prin_shop.model.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto productToProductDto(Product product);
    Product productDtoToProduct(ProductDto productDto);
    List<ProductDto> productsToProductDtos(List<Product> products);
}
