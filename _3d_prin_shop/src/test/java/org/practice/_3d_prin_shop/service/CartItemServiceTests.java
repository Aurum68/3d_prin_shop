package org.practice._3d_prin_shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.practice._3d_prin_shop.model.CartItem;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.repository.CartItemRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTests {

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemService cartItemService;

    @Test
    void testGetAllCartItems() {
        CartItem cartItem1 = new CartItem();
        CartItem cartItem2 = new CartItem();

        List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2);

        Mockito.when(cartItemRepository.findAll()).thenReturn(cartItems);

        List<CartItem> result = cartItemService.getAllCartItems();

        Assertions.assertEquals(cartItems, result);
        Mockito.verify(cartItemRepository).findAll();
    }

    @Test
    void testGetCartItemById() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);

        Mockito.when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));

        CartItem result = cartItemService.getCartItemById(1L);
        Assertions.assertEquals(cartItem, result);
        Mockito.verify(cartItemRepository).findById(1L);
    }

    @Test
    void testAddCartItem() {
        CartItem cartItem = new CartItem();

        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        CartItem result = cartItemService.addCartItem(cartItem);

        Assertions.assertEquals(cartItem, result);
        Mockito.verify(cartItemRepository).save(cartItem);
    }

    @Test
    void testUpdateCartItem() {
        Product product = new Product();
        product.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);

        CartItem updates = new CartItem();
        updates.setProduct(product);
        updates.setQuantity(3);

        Mockito.when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        CartItem result = cartItemService.updateCartItem(1L, updates);

        Assertions.assertEquals(cartItem.getProduct(), result.getProduct());
        Assertions.assertEquals(cartItem.getQuantity(), result.getQuantity());

        Mockito.verify(cartItemRepository).findById(1L);
        Mockito.verify(cartItemRepository).save(cartItem);
    }

    @Test
    void testDeleteCartItem() {
        cartItemService.deleteCartItem(1L);
        Mockito.verify(cartItemRepository).deleteById(1L);
    }
}
