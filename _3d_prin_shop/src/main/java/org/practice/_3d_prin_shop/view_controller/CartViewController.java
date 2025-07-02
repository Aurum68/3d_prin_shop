package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.model.Cart;
import org.practice._3d_prin_shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartViewController {

    private final CartService cartService;

    @Autowired
    public CartViewController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{cartId}")
    public String cart(@PathVariable Long cartId, Model model) {
        Cart cart = cartService.getCartById(cartId);
        if (cart == null) throw new RuntimeException("Cart not found");
        model.addAttribute("cart", cart);
        return "cart";
    }
}
