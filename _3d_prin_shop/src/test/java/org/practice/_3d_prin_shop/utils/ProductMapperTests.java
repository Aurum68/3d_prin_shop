package org.practice._3d_prin_shop.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.practice._3d_prin_shop.dto.ProductDto;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ProductMapperTests {

    @Autowired
    private ProductMapper productMapper;

    @Test
    void testProductToProductDto(){
        Product product = new Product();
        product.setId(1L);
        product.setName("test");
        product.setDescription("test");
        product.setPrice(new BigDecimal(50));
        product.setStock(10);

        ProductDto productDto = productMapper.productToProductDto(product);

        Assertions.assertEquals(product.getId(), productDto.getId());
        Assertions.assertEquals(product.getName(), productDto.getName());
        Assertions.assertEquals(product.getDescription(), productDto.getDescription());
        Assertions.assertEquals(product.getPrice(), productDto.getPrice());
        Assertions.assertEquals(product.getStock(), productDto.getStock());
    }

    @Test
    void testProductDtoToProduct(){
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("test");
        productDto.setDescription("test");
        productDto.setPrice(new BigDecimal(50));
        productDto.setStock(10);

        Product product = productMapper.productDtoToProduct(productDto);

        Assertions.assertEquals(product.getId(), productDto.getId());
        Assertions.assertEquals(product.getName(), productDto.getName());
        Assertions.assertEquals(product.getDescription(), productDto.getDescription());
        Assertions.assertEquals(product.getPrice(), productDto.getPrice());
        Assertions.assertEquals(product.getStock(), productDto.getStock());
    }

    @Test
    void testToProductDtoList(){
        Product product = new Product();
        product.setId(1L);

        Product product1 = new Product();
        product1.setId(2L);

        List<Product> products = Arrays.asList(product, product1);

        List<ProductDto> productDtos = productMapper.toDtoList(products);

        Assertions.assertEquals(productDtos.size(), products.size());
        Assertions.assertEquals(productDtos.get(0).getId(), products.get(0).getId());
        Assertions.assertEquals(productDtos.get(1).getId(), products.get(1).getId());
    }
}
