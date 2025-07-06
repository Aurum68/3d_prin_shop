package org.practice._3d_prin_shop.rest_controller;

import org.practice._3d_prin_shop.dto.ProductDto;
import org.practice._3d_prin_shop.model.Cart;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.request.AddToCartRequest;
import org.practice._3d_prin_shop.service.CartService;
import org.practice._3d_prin_shop.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    private final CartService cartService;
    private final ProductMapper productMapper;

    @Autowired
    public CartRestController(CartService cartService, ProductMapper productMapper) {
        this.cartService = cartService;
        this.productMapper = productMapper;
    }

    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Long userId) {return cartService.getCartById(userId);}

    @PostMapping("/{cartId}/add")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Long cartId, @RequestBody AddToCartRequest request) {
        ProductDto productDto = request.getProduct();
        Product product = productMapper.productDtoToProduct(productDto);
        try {
            Cart cart = cartService.addItemToCart(cartId, product, request.getQuantity());
            return ResponseEntity.ok(cart);
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("{cartId}/item/{cartItemId}")
    public Cart updateQuantity(@PathVariable Long cartId, @PathVariable Long cartItemId, @RequestBody int quantity) {
        return cartService.updateCartItemQuantity(cartId, cartItemId, quantity);
    }

    @DeleteMapping("/{cartId}/item/{cartItemId}")
    public void deleteItemFromCart(@PathVariable Long cartId, @PathVariable Long cartItemId) {
        cartService.deleteCartItem(cartId, cartItemId);
    }
}
