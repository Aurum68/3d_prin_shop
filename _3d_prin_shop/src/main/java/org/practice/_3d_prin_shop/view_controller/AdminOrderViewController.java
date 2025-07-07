package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.model.Order;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.security.UserPrincipal;
import org.practice._3d_prin_shop.service.OrderService;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderViewController {
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public AdminOrderViewController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String order(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "admin/order";
    }

    @GetMapping("orders_of{userId}")
    public String ordersOfUser(@PathVariable Long userId, Model model) {
        List<Order> orders = orderService.getByUserId(userId);
        model.addAttribute("orders", orders);
        return "admin/orders";
    }

    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/all";
    }

    @GetMapping("/new_orders")
    public String newOrders(Model model) {
        List<Order> orders = orderService.getByStatus(OrderStatus.PENDING_APPROVAL.getStatus());
        model.addAttribute("orders", orders);
        return "admin/new_orders";
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id,
                         @AuthenticationPrincipal UserPrincipal userPrincipal,
                         RedirectAttributes redirectAttributes) {

        User user = userService.getUserByUsername(userPrincipal.getUsername());
        Order order = orderService.cancelOrder(id, user);
        redirectAttributes.addFlashAttribute("order", order);
        return "redirect:/admin/order/" + order.getId();
    }

    @PostMapping("/{id}/activate")
    public String activate(@PathVariable Long id,
                           @AuthenticationPrincipal UserPrincipal userPrincipal,
                           RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(userPrincipal.getUsername());
        Order order = orderService.setOrderStatus(id, OrderStatus.ACTIVE, user);
        redirectAttributes.addFlashAttribute("order", order);
        return "redirect:/admin/order/" + order.getId();
    }
}
