package org.practice._3d_prin_shop.rest_controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.practice._3d_prin_shop.dto.ProductDto;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.service.ProductService;
import org.practice._3d_prin_shop.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductRestControllerTests {

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductMapper productMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllProducts() throws Exception {
        Product product = new Product();
        product.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);

        List<Product> products = Arrays.asList(product, product2);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        ProductDto productDto2 = new ProductDto();
        productDto2.setId(2L);

        List<ProductDto> productDtoList = Arrays.asList(productDto, productDto2);

        Mockito.when(productService.getAllProducts()).thenReturn(products);
        Mockito.when(productMapper.toDtoList(products)).thenReturn(productDtoList);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = new Product();
        product.setId(1L);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);

        Mockito.when(productService.getProductById(1L)).thenReturn(product);
        Mockito.when(productMapper.productToProductDto(product)).thenReturn(productDto);

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }
}
