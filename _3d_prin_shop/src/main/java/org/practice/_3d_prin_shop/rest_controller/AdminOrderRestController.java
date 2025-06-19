package org.practice._3d_prin_shop.rest_controller;

import org.practice._3d_prin_shop.model.Order;
import org.practice._3d_prin_shop.service.OrderService;
import org.practice._3d_prin_shop.util.OrderMapper;
import org.practice._3d_prin_shop.util.OrderStatus;
import org.practice._3d_prin_shop.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/order")
public class AdminOrderRestController {

    private final OrderService orderService;

    @Autowired
    public AdminOrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {return orderService.findAll();}

    @GetMapping("/orders_of{userId}")
    public List<Order> getOrdersOfUser(@PathVariable Long userId) {return orderService.findByUserId(userId);}

    @PutMapping("/{orderId}")
    public Order updateOrder(@PathVariable Long orderId, @RequestBody Order order) {
        return orderService.updateOrder(orderId, order);
    }
}
