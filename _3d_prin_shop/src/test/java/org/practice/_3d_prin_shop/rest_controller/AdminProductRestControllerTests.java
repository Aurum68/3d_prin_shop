package org.practice._3d_prin_shop.rest_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminProductRestController.class)
public class AdminProductRestControllerTests {

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = "admin")
    void testAddProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("test");
        product.setDescription("desc");

        Mockito.when(productService.addProduct(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/admin/products/add")
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.description").value("desc"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "admin")
    void testUpdateProduct() throws Exception {

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("test");
        updatedProduct.setDescription("desc");

        Mockito.when(productService.updateProduct(Mockito.eq(1L), Mockito.any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/admin/products/{id}", 1L)
                .content(objectMapper.writeValueAsString(updatedProduct))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.description").value("desc"));

        Mockito.verify(productService).updateProduct(Mockito.eq(1L), Mockito.any(Product.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "admin")
    void testDeleteProduct() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(Mockito.anyLong());

        mockMvc.perform(delete("/api/admin/products/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Mockito.verify(productService).deleteProduct(Mockito.anyLong());
    }
}
