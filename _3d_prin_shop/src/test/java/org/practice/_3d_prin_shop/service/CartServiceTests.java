package org.practice._3d_prin_shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.practice._3d_prin_shop.model.Cart;
import org.practice._3d_prin_shop.model.CartItem;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CartServiceTests {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemService cartItemService;

    @InjectMocks
    private CartService cartService;

    @Test
    void testGetCartById(){
        Cart cart = new Cart();
        cart.setId(1L);

        Mockito.when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartById(1L);

        Assertions.assertEquals(cart, result);
        Mockito.verify(cartRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testAddNewItemToCart(){
        Cart cart = new Cart();
        cart.setId(1L);

        Product product = new Product();
        product.setId(1L);

        int quantity = 1;

        List<CartItem> cartItems = new ArrayList<>();

        cart.setCartItems(cartItems);

        Mockito.when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);

        Cart result = cartService.addItemToCart(1L, product, quantity);

        Assertions.assertEquals(cart, result);
        Assertions.assertEquals(1, cart.getCartItems().size());

        CartItem cartItem = cart.getCartItems().getFirst();

        Assertions.assertEquals(product, cartItem.getProduct());
        Assertions.assertEquals(quantity, cartItem.getQuantity());

        Mockito.verify(cartRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
        Mockito.verify(cartItemService).addCartItem(Mockito.any(CartItem.class));
        Mockito.verify(cartItemService, Mockito.never()).updateCartItem(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void testAddExistingItemToCart(){
        Cart cart = new Cart();
        cart.setId(1L);

        Product product = new Product();
        product.setId(1L);

        int quantity = 1;

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setCart(cart);

        cart.setCartItems(List.of(cartItem));

        Mockito.when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        Mockito.when(cartItemService.getCartItemById(1L)).thenReturn(cartItem);

        int addedQuantity = 3;
        Cart result = cartService.addItemToCart(1L, product, addedQuantity);

        Assertions.assertEquals(cart, result);
        Assertions.assertEquals(1, cart.getCartItems().size());
        Assertions.assertEquals(addedQuantity + quantity, cart.getCartItems().getFirst().getQuantity());

        Mockito.verify(cartItemService).updateCartItem(cartItem.getId(), cartItem);
        Mockito.verify(cartItemService, Mockito.never()).addCartItem(Mockito.any());
        Mockito.verify(cartRepository).save(cart);
    }

    @Test
    void testUpdateCartItemQuantity(){
        Cart cart = new Cart();
        cart.setId(1L);

        List<CartItem> cartItems = new ArrayList<>();

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(new Product());
        cartItem.setQuantity(1);
        cartItem.setCart(cart);

        cartItems.add(cartItem);
        cart.setCartItems(cartItems);

        Mockito.when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        Mockito.when(cartItemService.getCartItemById(1L)).thenReturn(cartItem);

        int newQuantity = 2;
        Cart result = cartService.updateCartItemQuantity(1L, 1L, newQuantity);

        Assertions.assertEquals(cart, result);
        Assertions.assertEquals(newQuantity, cartItem.getQuantity());

        Mockito.verify(cartRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
        Mockito.verify(cartItemService).getCartItemById(1L);
        Mockito.verify(cartItemService).updateCartItem(cartItem.getId(), cartItem);
    }

    @Test
    void testDeleteCartItem(){
        Cart cart = new Cart();
        cart.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);

        Mockito.when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        Mockito.when(cartItemService.getCartItemById(1L)).thenReturn(cartItem);

        cartService.deleteCartItem(cart.getId(), cartItem.getId());
        Mockito.verify(cartItemService).deleteCartItem(1L);
    }
}
