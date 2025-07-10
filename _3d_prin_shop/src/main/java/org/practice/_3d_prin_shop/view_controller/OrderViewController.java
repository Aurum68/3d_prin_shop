package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.model.Order;
import org.practice._3d_prin_shop.service.OrderService;
import org.practice._3d_prin_shop.util.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderViewController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderViewController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping("/{orderId}")
    public String showOrder(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) return "error/404";

        model.addAttribute("order", orderMapper.orderToOrderDto(order));
        return "order";
    }

    @GetMapping("/orders_of/{userId}")
    public String showOrdersOfUser(@PathVariable Long userId, Model model) {
        List<Order> orders = orderService.getByUserId(userId);
        model.addAttribute("orders", orderMapper.toDtoList(orders));
        return "orders";
    }

    @PostMapping("/create/{cartId}")
    public String createOrder(@PathVariable Long cartId, RedirectAttributes redirectAttributes) {
        Order order = orderService.createOrderFromCart(cartId);
        redirectAttributes.addFlashAttribute("order", orderMapper.orderToOrderDto(order));
        return "redirect:/order/" + order.getId();
    }
}
