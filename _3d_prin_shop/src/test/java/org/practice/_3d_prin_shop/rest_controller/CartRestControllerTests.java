package org.practice._3d_prin_shop.rest_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.practice._3d_prin_shop.dto.ProductDto;
import org.practice._3d_prin_shop.model.Cart;
import org.practice._3d_prin_shop.model.CartItem;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.request.AddToCartRequest;
import org.practice._3d_prin_shop.service.CartService;
import org.practice._3d_prin_shop.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CartRestController.class)
public class CartRestControllerTests {
    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private ProductMapper productMapper;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testGetCart() throws Exception {
        Cart cart = new Cart();
        cart.setId(1L);

        Mockito.when(cartService.getCartById(1L)).thenReturn(cart);

        mockMvc.perform(get("/api/cart/{id}", 1L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        Mockito.verify(cartService).getCartById(1L);
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testAddItemToCart_success() throws Exception {
        Cart cart = new Cart();
        cart.setId(1L);

        Product product = new Product();
        product.setId(1L);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);

        AddToCartRequest request = new AddToCartRequest();
        request.setId(1L);
        request.setProduct(productDto);
        request.setQuantity(5);

        Mockito.when(productMapper.productDtoToProduct(Mockito.any(ProductDto.class))).thenReturn(product);
        Mockito.when(cartService.addItemToCart(1L, product, 5)).thenReturn(cart);

        mockMvc.perform(post("/api/cart/{cartId}/add", 1L)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        Mockito.verify(productMapper).productDtoToProduct(Mockito.any(ProductDto.class));
        Mockito.verify(cartService).addItemToCart(1L, product, 5);
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testAddItemToCart_failure() throws Exception {
        Cart cart = new Cart();
        cart.setId(1L);

        Product product = new Product();
        product.setId(1L);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);

        AddToCartRequest request = new AddToCartRequest();
        request.setId(1L);
        request.setProduct(productDto);
        request.setQuantity(5);

        Mockito.when(productMapper.productDtoToProduct(Mockito.any(ProductDto.class))).thenReturn(product);
        Mockito.when(cartService.addItemToCart(1L, product, 5)).thenThrow(new AccessDeniedException(String.format("Вы заблокированы. Причина: %s", cart.getUser().getBlockedReason())));

        mockMvc.perform(post("/api/cart/{cartId}/add", 1L)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testUpdateCart() throws Exception {
        Cart cart = new Cart();
        cart.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(5);

        int newQuantity = 5;

        cart.setCartItems(List.of(cartItem));

        Mockito.when(cartService.updateCartItemQuantity(1L, 1L, newQuantity)).thenAnswer(
                invocationOnMock -> {cart.getCartItems().getFirst().setQuantity(cartItem.getQuantity() + newQuantity);
                                                    return cart;}
        );

        mockMvc.perform(put("/api/cart/{cartId}/item/{cartItemId}", 1L, 1L)
                .content(objectMapper.writeValueAsString(newQuantity))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        Mockito.verify(cartService).updateCartItemQuantity(1L, 1L, newQuantity);
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testDeleteItemFromCart() throws Exception {
        Mockito.doNothing().when(cartService).deleteCartItem(Mockito.anyLong(), Mockito.anyLong());

        mockMvc.perform(delete("/api/cart/{cartId}/item/{cartItemId}", 1L, 1L)
                .with(csrf()))
                .andExpect(status().isOk());

        Mockito.verify(cartService).deleteCartItem(1L, 1L);
    }
}
