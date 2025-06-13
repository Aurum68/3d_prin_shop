package org.practice._3d_prin_shop.service;

import org.practice._3d_prin_shop.model.CartItem;
import org.practice._3d_prin_shop.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository) {this.cartItemRepository = cartItemRepository;}

    public void addCartItem(CartItem cartItem) {cartItemRepository.save(cartItem);}

    public void updateCartItem(CartItem cartItem) {
        CartItem cartItemToUpdate = cartItemRepository.findById(cartItem.getId()).orElseThrow();

        cartItemToUpdate.setQuantity(cartItem.getQuantity());
        cartItemRepository.save(cartItemToUpdate);
    }

    public void deleteCartItem(CartItem cartItem) {cartItemRepository.deleteById(cartItem.getId());}

    public List<CartItem> getAllCartItems() {return cartItemRepository.findAll();}

    public CartItem getCartItemById(Long id) {return cartItemRepository.findById(id).orElseThrow();}
}
