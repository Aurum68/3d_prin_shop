package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.model.Order;
import org.practice._3d_prin_shop.service.OrderService;
import org.practice._3d_prin_shop.util.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        Order order = orderService.getOrder(orderId);
        if (order == null) return "error/404";

        model.addAttribute("order", orderMapper.orderToOrderDto(order));
        return "order";
    }
}
