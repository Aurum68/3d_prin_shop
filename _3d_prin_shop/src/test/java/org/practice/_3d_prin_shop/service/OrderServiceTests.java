package org.practice._3d_prin_shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.practice._3d_prin_shop.dto.OrderDto;
import org.practice._3d_prin_shop.model.*;
import org.practice._3d_prin_shop.repository.OrderRepository;
import org.practice._3d_prin_shop.repository.UserRepository;
import org.practice._3d_prin_shop.util.OrderMapper;
import org.practice._3d_prin_shop.util.OrderStatus;
import org.practice._3d_prin_shop.util.Roles;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testGetAllOrders() {
        Order order = new Order();
        Order order2 = new Order();

        List<Order> orders = Arrays.asList(order, order2);

        Mockito.when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        Assertions.assertEquals(orders, result);
        Mockito.verify(orderRepository).findAll();
    }

    @Test
    void testGetByUserId() {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        Order order = new Order();
        order.setUser(user);
        Order order2 = new Order();
        order2.setUser(user2);
        Order order3 = new Order();
        order3.setUser(user);

        List<Order> orders = Arrays.asList(order, order2, order3);

        Mockito.when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getByUserId(user.getId());

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(order, result.get(0));
        Assertions.assertEquals(order3, result.get(1));

        Mockito.verify(orderRepository).findAll();
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        order.setId(1L);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L);
        Assertions.assertEquals(order, result);
        Mockito.verify(orderRepository).findById(1L);
    }

    @Test
    void testCreateOrder_success() {
        User user = new User();
        user.setId(1L);
        user.setBlocked(false);

        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(user.getId());

        Order order = new Order();
        order.setUser(user);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(orderMapper.orderDtoToOrder(Mockito.any(OrderDto.class))).thenReturn(order);
        Mockito.when(orderRepository.save(order)).thenReturn(order);

        Order result;
        try {
            result = orderService.createOrder(orderDto);
        }catch (AccessDeniedException e){
            result = null;
            System.err.println(e.getMessage());
        }

        Assertions.assertEquals(order, result);
        Assertions.assertEquals(order.getStatus(), OrderStatus.PENDING_APPROVAL.getStatus());

        Mockito.verify(userRepository).findById(user.getId());
        Mockito.verify(orderMapper).orderDtoToOrder(orderDto);
        Mockito.verify(orderRepository).save(order);
    }

    @Test
    void testCreateOrder_fail() {
        User user = new User();
        user.setId(1L);
        user.setBlocked(true);
        user.setBlockedReason("Blocked Reason");

        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(user.getId());

        Order order = new Order();
        order.setUser(user);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(orderMapper.orderDtoToOrder(Mockito.any(OrderDto.class))).thenReturn(order);

        Assertions.assertThrows(AccessDeniedException.class, () -> orderService.createOrder(orderDto));
    }

    @Test
    void testAddNewItemToOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderItems(new ArrayList<>());

        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal(5));

        int quantity = 3;

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.addItemToOrder(1L, product, quantity);

        Assertions.assertEquals(order, result);
        Assertions.assertEquals(1, order.getOrderItems().size());

        Mockito.verify(orderRepository).findById(1L);
        Mockito.verify(orderRepository).save(order);
    }

    @Test
    void testAddItemToOrder_WhenProductAlreadyExists() {
        Order order = new Order();
        order.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal(5));

        int quantity = 3;

        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(quantity);
        orderItem.setProduct(product);

        order.setOrderItems(List.of(orderItem));

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Assertions.assertThrows(IllegalArgumentException.class, () -> orderService.addItemToOrder(1L, product, quantity));

        Mockito.verify(orderRepository).findById(1L);
        Mockito.verifyNoMoreInteractions(orderRepository, orderItemService);
    }

    @Test
    void testSetOrderStatus_success() throws AccessDeniedException {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING_APPROVAL.getStatus());

        User user = new User();
        user.setId(1L);
        user.setRole(Roles.ROLE_ADMIN.getRole());

        order.setUser(user);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.setOrderStatus(1L, OrderStatus.ACTIVE, user);

        Assertions.assertEquals(order, result);
        Assertions.assertEquals(result.getStatus(), OrderStatus.ACTIVE.getStatus());

        Mockito.verify(orderRepository).findById(1L);
        Mockito.verify(orderRepository).save(order);
    }

    @Test
    void testSetOrderStatus_fail() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING_APPROVAL.getStatus());

        User user = new User();
        user.setId(1L);
        user.setRole(Roles.ROLE_USER.getRole());

        order.setUser(user);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Assertions.assertThrows(AccessDeniedException.class, () -> orderService.setOrderStatus(1L, OrderStatus.ACTIVE, user));

        Mockito.verify(orderRepository).findById(1L);
    }

    @Test
    void testCancelOrder_success() throws AccessDeniedException {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING_APPROVAL.getStatus());

        User user = new User();
        user.setId(1L);
        user.setRole(Roles.ROLE_USER.getRole());

        order.setUser(user);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.cancelOrder(1L, user);

        Assertions.assertEquals(order, result);
        Assertions.assertEquals(OrderStatus.CANCELLED.getStatus(), result.getStatus());

        Mockito.verify(orderRepository).findById(1L);
        Mockito.verify(orderRepository).save(order);
    }
}
