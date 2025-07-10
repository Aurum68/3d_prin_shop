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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        List<OrderStatus> statuses = Arrays.stream(OrderStatus.values())
                        .filter(status -> status != OrderStatus.PENDING_APPROVAL).toList();
        model.addAttribute("order", order);
        model.addAttribute("statuses", statuses);
        return "admin/order";
    }

    @GetMapping("/orders_of/{userId}")
    public String ordersOfUser(@PathVariable Long userId, Model model) {
        List<Order> orders = orderService.getByUserId(userId);
        model.addAttribute("orders", orders);
        model.addAttribute("user", userService.getUserById(userId));
        return "admin/orders-of-user";
    }

    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders-all";
    }

    @GetMapping("/new_orders")
    public String newOrders(Model model) {
        List<Order> orders = orderService.getByStatus(OrderStatus.PENDING_APPROVAL.getStatus());
        model.addAttribute("orders", orders);
        return "admin/orders-new";
    }

    @PostMapping("/{id}/update-status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam("status") String status,
                               @AuthenticationPrincipal UserPrincipal user,
                               RedirectAttributes redirectAttributes) {
        User currentUser = userService.getUserByUsername(user.getUsername());
        Order order = orderService.setOrderStatus(id, OrderStatus.fromString(status), currentUser);
        redirectAttributes.addFlashAttribute("order", order);
        return "redirect:/admin/order/all";
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
