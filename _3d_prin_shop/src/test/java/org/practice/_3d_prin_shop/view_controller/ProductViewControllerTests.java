package org.practice._3d_prin_shop.view_controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.practice._3d_prin_shop.dto.ProductDto;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.service.ProductService;
import org.practice._3d_prin_shop.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = ProductViewController.class)
public class ProductViewControllerTests {

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductMapper productMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testProducts() throws Exception {
        List<Product> products = List.of(new Product());
        List<ProductDto> productDtoList = List.of(new ProductDto());

        Mockito.when(productService.getAllProducts()).thenReturn(products);
        Mockito.when(productMapper.toDtoList(products)).thenReturn(productDtoList);

        mockMvc.perform(get("/products"));
    }
}
