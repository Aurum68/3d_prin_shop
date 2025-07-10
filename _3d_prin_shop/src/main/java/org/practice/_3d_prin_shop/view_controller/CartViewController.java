package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.model.Cart;
import org.practice._3d_prin_shop.model.CartItem;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.request.AddToCartRequest;
import org.practice._3d_prin_shop.service.CartItemService;
import org.practice._3d_prin_shop.service.CartService;
import org.practice._3d_prin_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartViewController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ProductService productService;

    @Autowired
    public CartViewController(CartService cartService, CartItemService cartItemService, ProductService productService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    @GetMapping("/{cartId}")
    public String cart(@PathVariable Long cartId, Model model) {
        Cart cart = cartService.getCartById(cartId);
        System.err.println(cart + " " + cartId);
        if (cart == null) throw new RuntimeException("Cart not found");
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("{cartId}/add")
    public String addToCart(@PathVariable Long cartId, @ModelAttribute AddToCartRequest request) {
        Product product = productService.getProductById(request.getId());
        cartService.addItemToCart(cartId, product, request.getQuantity());
        return "redirect:/cart/" + cartId;
    }

    @PostMapping("/{cartId}/update/{productId}")
    public String updateQuantity(@PathVariable Long cartId, @PathVariable Long productId, @RequestParam("quantity") int quantity) {
        Cart cart = cartService.getCartById(cartId);
        CartItem cartItem = cartItemService.getCartItemByCartAndProduct(cart, productService.getProductById(productId));
        cartService.updateCartItemQuantity(cartId, cartItem.getId(), quantity);
        return "redirect:/cart/" + cartId;
    }

    @PostMapping("/{cartId}/remove/{productId}")
    public String removeFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        Cart cart = cartService.getCartById(cartId);
        CartItem cartItem = cartItemService.getCartItemByCartAndProduct(cart, productService.getProductById(productId));
        cartService.deleteCartItem(cartId, cartItem.getId());
        return "redirect:/cart/" + cartId;
    }
}
