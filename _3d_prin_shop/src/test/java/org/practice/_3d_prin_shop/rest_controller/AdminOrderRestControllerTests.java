package org.practice._3d_prin_shop.rest_controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.practice._3d_prin_shop.model.Order;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.security.UserPrincipal;
import org.practice._3d_prin_shop.service.OrderService;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.OrderStatus;
import org.practice._3d_prin_shop.util.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminOrderRestController.class)
public class AdminOrderRestControllerTests {

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"admin"})
    void testGetAllOrders() throws Exception {
        Order order = new Order();
        order.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);

        List<Order> orders = Arrays.asList(order, order2);

        Mockito.when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/admin/order/all")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order.getId()))
                .andExpect(jsonPath("$[1].id").value(order2.getId()));

        Mockito.verify(orderService).getAllOrders();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"admin"})
    void testGetOrderByUserId() throws Exception {
        Order order = new Order();
        order.setId(1L);

        Order order2 = new Order();
        order2.setId(2L);

        User user = new User();
        user.setId(1L);

        order2.setUser(user);
        order.setUser(user);

        List<Order> orders = Arrays.asList(order, order2);

        Mockito.when(orderService.getByUserId(1L)).thenReturn(orders);

        mockMvc.perform(get("/api/admin/order/orders_of{userId}", 1L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order.getId()))
                .andExpect(jsonPath("$[1].id").value(order2.getId()));

        Mockito.verify(orderService).getByUserId(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"admin"})
    void testCancelOrder_success() throws Exception {
        Order order = new Order();
        order.setId(1L);

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setRole(Roles.ROLE_ADMIN.getRole());

        UserPrincipal principal = new UserPrincipal(user);

        Mockito.when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(orderService.cancelOrder(Mockito.eq(1L), Mockito.any(User.class))).thenReturn(order);

        mockMvc.perform(patch("/api/admin/order/{orderId}/cancel", order.getId())
                        .with(user(principal))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()));


        Mockito.verify(userService).getUserByUsername(user.getUsername());
        Mockito.verify(orderService).cancelOrder(Mockito.eq(1L), Mockito.any(User.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"user"})
    void testCancelOrder_failure() throws Exception {
        Order order = new Order();
        order.setId(1L);

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setRole(Roles.ROLE_USER.getRole());

        UserPrincipal principal = new UserPrincipal(user);

        Mockito.when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(orderService.cancelOrder(Mockito.eq(1L), Mockito.any(User.class))).thenThrow(new AccessDeniedException(""));

        mockMvc.perform(patch("/api/admin/order/{orderId}/cancel", order.getId())
        .with(user(principal))
        .with(csrf()))
                .andExpect(status().isForbidden());

        Mockito.verify(userService).getUserByUsername(user.getUsername());
        Mockito.verify(orderService).cancelOrder(Mockito.eq(1L), Mockito.any(User.class));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"admin"})
    void testActivateOrder_success() throws Exception {
        Order order = new Order();
        order.setId(1L);

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setRole(Roles.ROLE_ADMIN.getRole());

        UserPrincipal principal = new UserPrincipal(user);

        Mockito.when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(orderService.setOrderStatus(Mockito.eq(1L), Mockito.eq(OrderStatus.ACTIVE), Mockito.any(User.class))).thenReturn(order);

        mockMvc.perform(patch("/api/admin/order/{orderId}/activate", order.getId())
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()));


        Mockito.verify(userService).getUserByUsername(user.getUsername());
        Mockito.verify(orderService).setOrderStatus(Mockito.eq(1L), Mockito.eq(OrderStatus.ACTIVE), Mockito.any(User.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"user"})
    void testActivateOrder_failure() throws Exception {
        Order order = new Order();
        order.setId(1L);

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setRole(Roles.ROLE_USER.getRole());

        UserPrincipal principal = new UserPrincipal(user);

        Mockito.when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(orderService.setOrderStatus(Mockito.eq(1L), Mockito.eq(OrderStatus.ACTIVE), Mockito.any(User.class))).thenThrow(new AccessDeniedException(""));

        mockMvc.perform(patch("/api/admin/order/{orderId}/activate", order.getId())
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isForbidden());

        Mockito.verify(userService).getUserByUsername(user.getUsername());
        Mockito.verify(orderService).setOrderStatus(Mockito.eq(1L), Mockito.eq(OrderStatus.ACTIVE), Mockito.any(User.class));

    }
}
