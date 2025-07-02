package org.practice._3d_prin_shop.service;

import org.practice._3d_prin_shop.model.CartItem;
import org.practice._3d_prin_shop.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository) {this.cartItemRepository = cartItemRepository;}

    public CartItem addCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
        return cartItem;
    }

    public CartItem updateCartItem(Long id, CartItem cartItem) {
        CartItem cartItemToUpdate = cartItemRepository.findById(id).orElseThrow();

        if (!Objects.equals(cartItem.getProduct().getId(), cartItemToUpdate.getProduct().getId())) throw new IllegalArgumentException("Product id not match");
        cartItemToUpdate.setQuantity(cartItem.getQuantity());
        cartItemRepository.save(cartItemToUpdate);
        return cartItemToUpdate;
    }

    public void deleteCartItem(Long id) {cartItemRepository.deleteById(id);}

    public List<CartItem> getAllCartItems() {return cartItemRepository.findAll();}

    public CartItem getCartItemById(Long id) {return cartItemRepository.findById(id).orElseThrow();}
}
