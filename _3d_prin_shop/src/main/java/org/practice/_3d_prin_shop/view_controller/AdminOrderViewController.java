package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderViewController {
    private final OrderService orderService;

    @Autowired
    public AdminOrderViewController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String order(Model model) {
        model.addAllAttributes(orderService.getAllOrders());
        return "admin/order";
    }
}
