package org.practice._3d_prin_shop.service;

import org.practice._3d_prin_shop.model.Cart;
import org.practice._3d_prin_shop.model.CartItem;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public Cart getCartById(Long userId) {return cartRepository.findById(userId).orElseThrow();}



    public Cart addItemToCart(Long cartId, Product product, int quantity) throws AccessDeniedException {
        Cart cart = getCartById(cartId);

        if (cart.getUser().isBlocked()) throw new AccessDeniedException(String.format("Вы заблокированы. Причина: %s", cart.getUser().getBlockedReason()));

        Optional<CartItem> byProduct = cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();

        if (byProduct.isPresent()) {

            CartItem item = byProduct.get();
            updateCartItemQuantity(cartId, item.getId(), item.getQuantity() + quantity);

        }else{
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setCart(cart);
            cartItemService.addCartItem(item);

            cart.getItems().add(item);
            cart.setTotal_price(getCartTotal(cartId));
            cartRepository.save(cart);
        }
        return cart;
    }

    public Cart updateCartItemQuantity(Long cartId, Long cartItemId, int quantity) {
        Cart cart = getCartById(cartId);
        CartItem cartItem = cartItemService.getCartItemById(cartItemId);
        if (!cartItem.getCart().equals(cart)) throw new RuntimeException("CartItem is not in the cart");

        cartItem.setQuantity(quantity);
        cartItemService.updateCartItem(cartItem.getId(), cartItem);

        cart.setTotal_price(getCartTotal(cartId));
        cartRepository.save(cart);
        return cart;
    }

    public void deleteCartItem(Long cartId, Long cartItemId) {
        Cart cart = getCartById(cartId);
        if (!cartItemService.getCartItemById(cartItemId).getCart().equals(cart)) throw new RuntimeException("CartItem is not in the cart");
        cartItemService.deleteCartItem(cartItemId);
        cart.setTotal_price(getCartTotal(cartId));
        cartRepository.save(cart);
    }

    public Cart clearCart(Long cartId) {
        Cart cart = getCartById(cartId);
        for (CartItem item : cart.getItems()) {
            cartItemService.deleteCartItem(item.getId());
        }
        cart.getItems().clear();
        cart.setTotal_price(getCartTotal(cartId));
        return cartRepository.save(cart);
    }

    private BigDecimal getCartTotal(Long cartId) {
        Cart cart = getCartById(cartId);
        BigDecimal total = new BigDecimal(0);
        for (CartItem item : cart.getItems()) {
            total = total.add(item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }
}
