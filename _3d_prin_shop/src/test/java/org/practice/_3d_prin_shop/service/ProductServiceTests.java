package org.practice._3d_prin_shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        Product product2 = new Product();

        List<Product> products = Arrays.asList(product1, product2);
        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        Assertions.assertEquals(products, result);
        Mockito.verify(productRepository).findAll();
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setId(1L);

        Mockito.when(productRepository.getProductById(1L)).thenReturn(product);

        Product result = productService.getProductById(1L);

        Assertions.assertEquals(product, result);
        Mockito.verify(productRepository).getProductById(1L);
    }

    @Test
    void testAddProduct() {
        Product product = new Product();
        product.setId(1L);

        Mockito.when(productRepository.save(product)).thenReturn(product);

        Product result = productService.addProduct(product);
        Assertions.assertEquals(product, result);
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setId(1L);

        Product updatedProduct = new Product();
        updatedProduct.setPrice(new BigDecimal(1000));
        Mockito.when(productRepository.getProductById(1L)).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        Product result = productService.updateProduct(1L, updatedProduct);

        Assertions.assertEquals(product.getPrice(), result.getPrice());
        Mockito.verify(productRepository).getProductById(1L);
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void testDeleteProduct() {
        productService.deleteProduct(1L);
        Mockito.verify(productRepository).deleteProductById(1L);
    }
}
