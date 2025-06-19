package org.practice._3d_prin_shop.service;

import org.practice._3d_prin_shop.model.Cart;
import org.practice._3d_prin_shop.model.CartItem;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
    }

    public Cart getCart(Long userId) {return cartRepository.findById(userId).orElseThrow();}

    public Cart addItemToCart(Long cartId, Product product, int quantity) {
        Cart cart = getCart(cartId);

        Optional<CartItem> byProduct = cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();

        if (byProduct.isPresent()) {

            CartItem item = byProduct.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemService.updateCartItem(item);

        }else{

            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setCart(cart);
            cartItemService.addCartItem(item);

            cart.getCartItems().add(item);
            cartRepository.save(cart);
        }

        return cart;
    }

    public Cart updateCartItemQuantity(Long cartId, Long cartItemId, int quantity) {
        Cart cart = getCart(cartId);
        CartItem cartItem = cartItemService.getCartItemById(cartItemId);
        if (!cartItem.getCart().equals(cart)) throw new RuntimeException("CartItem is not in the cart");

        cartItem.setQuantity(quantity);
        cartItemService.updateCartItem(cartItem);
        return cart;
    }

    public void deleteCartItem(Long cartId, Long cartItemId) {
        Cart cart = getCart(cartId);
        if (!cartItemService.getCartItemById(cartItemId).getCart().equals(cart)) throw new RuntimeException("CartItem is not in the cart");
        cartItemService.deleteCartItem(cartItemService.getCartItemById(cartItemId));
    }
}
