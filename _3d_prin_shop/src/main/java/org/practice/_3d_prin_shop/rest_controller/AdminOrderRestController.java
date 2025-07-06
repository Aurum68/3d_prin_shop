package org.practice._3d_prin_shop.rest_controller;

import org.practice._3d_prin_shop.model.Order;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.security.UserPrincipal;
import org.practice._3d_prin_shop.service.OrderService;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/order")
@PreAuthorize("hasRole('admin')")
public class AdminOrderRestController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public AdminOrderRestController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {return orderService.getAllOrders();}

    @GetMapping("/orders_of{userId}")
    public List<Order> getOrdersOfUser(@PathVariable Long userId) {return orderService.getByUserId(userId);}

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userService.getUserByUsername(userPrincipal.getUsername());
        try {
            return ResponseEntity.ok(orderService.cancelOrder(orderId, user));
        }catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PatchMapping("/{orderId}/activate")
    public ResponseEntity<Order> activateOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserPrincipal user) {
        User currentUser = userService.getUserByUsername(user.getUsername());

        try {
            Order order = orderService.setOrderStatus(orderId, OrderStatus.ACTIVE, currentUser);
            return ResponseEntity.ok(order);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
