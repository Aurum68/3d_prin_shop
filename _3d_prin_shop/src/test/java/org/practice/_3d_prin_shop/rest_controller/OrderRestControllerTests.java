package org.practice._3d_prin_shop.rest_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.practice._3d_prin_shop.dto.OrderDto;
import org.practice._3d_prin_shop.dto.ProductDto;
import org.practice._3d_prin_shop.model.Order;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.request.AddToOrderRequest;
import org.practice._3d_prin_shop.security.UserPrincipal;
import org.practice._3d_prin_shop.service.OrderService;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.OrderMapper;
import org.practice._3d_prin_shop.util.OrderStatus;
import org.practice._3d_prin_shop.util.ProductMapper;
import org.practice._3d_prin_shop.util.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderRestController.class)
public class OrderRestControllerTests {

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private ProductMapper productMapper;

    @MockitoBean
    private OrderMapper orderMapper;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testGetOrdersOfUser() throws Exception {
        User user = new User();
        user.setId(1L);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        Order order2 = new Order();
        order2.setId(2L);
        order2.setUser(user);

        List<Order> orders = Arrays.asList(order, order2);
        user.setOrders(orders);

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(user.getId());
        OrderDto orderDto2 = new OrderDto();
        orderDto2.setId(2L);
        orderDto2.setUserId(user.getId());

        List<OrderDto> orderDtoList = Arrays.asList(orderDto, orderDto2);

        Mockito.when(orderService.getByUserId(user.getId())).thenReturn(orders);
        Mockito.when(orderMapper.toDtoList(Mockito.anyList())).thenReturn(orderDtoList);

        mockMvc.perform(get("/api/order/orders_of{userId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(orderDtoList.size())))
                .andExpect(jsonPath("$[0].id").value(orderDtoList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(orderDtoList.get(1).getId()));

        Mockito.verify(orderService).getByUserId(user.getId());
        Mockito.verify(orderMapper).toDtoList(Mockito.anyList());
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testGetOrder() throws Exception {
        User user = new User();
        user.setId(1L);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(user.getId());

        Mockito.when(orderService.getOrderById(user.getId())).thenReturn(order);
        Mockito.when(orderMapper.orderToOrderDto(Mockito.any(Order.class))).thenReturn(orderDto);

        mockMvc.perform(get("/api/order/{orderId}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderDto.getId()))
                .andExpect(jsonPath("$.userId").value(user.getId()));

        Mockito.verify(orderService).getOrderById(user.getId());
        Mockito.verify(orderMapper).orderToOrderDto(Mockito.any(Order.class));
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testCreateOrder_success() throws Exception {
        User user = new User();
        user.setId(1L);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(user.getId());

        Mockito.when(orderService.createOrder(Mockito.any(OrderDto.class))).thenReturn(order);
        Mockito.when(orderMapper.orderToOrderDto(Mockito.any(Order.class))).thenReturn(orderDto);

        mockMvc.perform(post("/api/order/add")
                .content(objectMapper.writeValueAsString(orderDto))
                .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderDto.getId()))
                .andExpect(jsonPath("$.userId").value(user.getId()));

        Mockito.verify(orderService).createOrder(Mockito.any(OrderDto.class));
        Mockito.verify(orderMapper).orderToOrderDto(Mockito.any(Order.class));
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testCreateOrder_fail() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);

        Mockito.when(orderService.createOrder(Mockito.any(OrderDto.class))).thenThrow(new AccessDeniedException(""));

        mockMvc.perform(post("/api/order/add")
                .content(objectMapper.writeValueAsString(orderDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isForbidden());

        Mockito.verify(orderService).createOrder(Mockito.any(OrderDto.class));
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testAddItemToOrder() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);

        Product product = new Product();
        product.setId(1L);

        Order order = new Order();
        order.setId(1L);

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);

        AddToOrderRequest addToOrderRequest = new AddToOrderRequest();
        addToOrderRequest.setProduct(productDto);
        addToOrderRequest.setQuantity(5);

        Mockito.when(productMapper.productDtoToProduct(Mockito.any(ProductDto.class))).thenReturn(product);
        Mockito.when(orderService.addItemToOrder(1L, product, 5)).thenReturn(order);
        Mockito.when(orderMapper.orderToOrderDto(Mockito.any(Order.class))).thenReturn(orderDto);

        mockMvc.perform(post("/api/order/{orderId}/add", order.getId())
                .content(objectMapper.writeValueAsString(addToOrderRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderDto.getId()));

        Mockito.verify(productMapper).productDtoToProduct(Mockito.any(ProductDto.class));
        Mockito.verify(orderService).addItemToOrder(1L, product, 5);
        Mockito.verify(orderMapper).orderToOrderDto(Mockito.any(Order.class));
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testSetOrderStatus_success() throws Exception {
        String status = "CANCELLED";

        Order order = new Order();
        order.setId(1L);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);

        User user = new User();
        user.setId(1L);
        user.setUsername("Bob");
        user.setPassword("password");
        user.setRole(Roles.ROLE_USER.getRole());

        order.setUser(user);

        UserPrincipal userPrincipal = new UserPrincipal(user);

        Mockito.when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(orderService.setOrderStatus(
                Mockito.eq(1L),
                Mockito.eq(OrderStatus.CANCELLED),
                Mockito.any(User.class)))
                .thenReturn(order);

        Mockito.when(orderMapper.orderToOrderDto(Mockito.any(Order.class))).thenReturn(orderDto);

        mockMvc.perform(patch("/api/order/{orderId}/status", order.getId())
                .param("status", status)
                .with(user(userPrincipal))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderDto.getId()));

        Mockito.verify(userService).getUserByUsername(user.getUsername());
        Mockito.verify(orderService).setOrderStatus(
                Mockito.eq(1L),
                Mockito.eq(OrderStatus.CANCELLED),
                Mockito.any(User.class));
        Mockito.verify(orderMapper).orderToOrderDto(Mockito.any(Order.class));
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testSetOrderStatus_failWithAccessDenied() throws Exception {
        String status = "ACTIVE";

        User user = new User();
        user.setId(1L);
        user.setUsername("Bob");
        user.setPassword("password");
        user.setRole(Roles.ROLE_USER.getRole());

        UserPrincipal userPrincipal = new UserPrincipal(user);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);

        Mockito.when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(orderService.setOrderStatus(
                Mockito.eq(1L),
                Mockito.eq(OrderStatus.ACTIVE),
                Mockito.any(User.class)
        )).thenThrow(new AccessDeniedException(""));

        mockMvc.perform(patch("/api/order/{orderId}/status", order.getId())
                .param("status", status)
                .with(user(userPrincipal))
                .with(csrf()))
                .andExpect(status().isForbidden());

        Mockito.verify(userService).getUserByUsername(user.getUsername());
        Mockito.verify(orderService).setOrderStatus(
                Mockito.eq(1L),
                Mockito.eq(OrderStatus.ACTIVE),
                Mockito.any(User.class)
        );
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testSetOrderStatus_failWithIllegalArgument() throws Exception {
        String status = "PENDING_APPROVAL";

        User user = new User();
        user.setId(1L);
        user.setUsername("Bob");
        user.setPassword("password");
        user.setRole(Roles.ROLE_USER.getRole());

        UserPrincipal userPrincipal = new UserPrincipal(user);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);

        Mockito.when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(orderService.setOrderStatus(
                Mockito.eq(1L),
                Mockito.eq(OrderStatus.PENDING_APPROVAL),
                Mockito.any(User.class)
        )).thenThrow(new IllegalArgumentException(""));

        mockMvc.perform(patch("/api/order/{orderId}/status", order.getId())
                .param("status", status)
                .with(user(userPrincipal))
                .with(csrf()))
                .andExpect(status().isBadRequest());

        Mockito.verify(userService).getUserByUsername(user.getUsername());
        Mockito.verify(orderService).setOrderStatus(
                Mockito.eq(1L),
                Mockito.eq(OrderStatus.PENDING_APPROVAL),
                Mockito.any(User.class)
        );
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testCancelOrder_success() throws Exception {
        Order order = new Order();
        order.setId(1L);

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);

        User user = new User();
        user.setId(1L);
        user.setUsername("Bob");
        user.setPassword("password");
        user.setRole(Roles.ROLE_USER.getRole());

        order.setUser(user);

        UserPrincipal userPrincipal = new UserPrincipal(user);

        Mockito.when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(orderService.cancelOrder(Mockito.eq(1L), Mockito.any(User.class))).thenReturn(order);
        Mockito.when(orderMapper.orderToOrderDto(Mockito.any(Order.class))).thenReturn(orderDto);

        mockMvc.perform(patch("/api/order/{orderId}/cancel", order.getId())
                .with(user(userPrincipal))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderDto.getId()));

        Mockito.verify(userService).getUserByUsername(user.getUsername());
        Mockito.verify(orderService).cancelOrder(Mockito.eq(1L), Mockito.any(User.class));
        Mockito.verify(orderMapper).orderToOrderDto(Mockito.any(Order.class));
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testCancelOrder_failWithAccessDenied() throws Exception {
        Order order = new Order();
        order.setId(1L);

        User user = new User();
        user.setId(1L);
        user.setUsername("Bob");
        user.setPassword("password");
        user.setRole(Roles.ROLE_USER.getRole());

        UserPrincipal userPrincipal = new UserPrincipal(user);

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Bill");
        user1.setPassword("password");
        user1.setRole(Roles.ROLE_USER.getRole());

        order.setUser(user1);

        Mockito.when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(orderService.cancelOrder(Mockito.eq(1L), Mockito.any(User.class))).thenThrow(new AccessDeniedException(""));

        mockMvc.perform(patch("/api/order/{orderId}/cancel", order.getId())
                .with(user(userPrincipal))
                .with(csrf()))
                .andExpect(status().isForbidden());

        Mockito.verify(userService).getUserByUsername(user.getUsername());
        Mockito.verify(orderService).cancelOrder(Mockito.eq(1L), Mockito.any(User.class));
    }
}
