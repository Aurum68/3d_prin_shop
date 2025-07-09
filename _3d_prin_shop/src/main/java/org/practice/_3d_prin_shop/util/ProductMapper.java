package org.practice._3d_prin_shop.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.practice._3d_prin_shop.dto.ProductDto;
import org.practice._3d_prin_shop.model.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto productToProductDto(Product product);
    Product productDtoToProduct(ProductDto productDto);
    List<ProductDto> toDtoList(List<Product> products);
}
