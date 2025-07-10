package org.practice._3d_prin_shop.service;

import org.practice._3d_prin_shop.dto.OrderDto;
import org.practice._3d_prin_shop.model.*;
import org.practice._3d_prin_shop.repository.CartRepository;
import org.practice._3d_prin_shop.repository.OrderRepository;
import org.practice._3d_prin_shop.repository.UserRepository;
import org.practice._3d_prin_shop.util.OrderMapper;
import org.practice._3d_prin_shop.util.OrderStatus;
import org.practice._3d_prin_shop.util.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemService orderItemService, CartRepository cartRepository, UserRepository userRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
    }

    public List<Order> getAllOrders() {return orderRepository.findAll();}

    public List<Order> getByUserId(Long userId) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Order> getByStatus(String status) {
        return orderRepository.findByStatus(status).orElseThrow();
    }

    public Order getOrderById(Long id) {return orderRepository.findById(id).orElseThrow();}

    public Order createOrderFromCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow();
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cart.getItems()){
            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setProduct(item.getProduct());
            newOrderItem.setQuantity(item.getQuantity());
            newOrderItem.setPrice(item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())));
            newOrderItem.setOrder(order);
            orderItems.add(newOrderItem);
        }
        order.setOrderItems(orderItems);
        order.setStatus(OrderStatus.PENDING_APPROVAL.getStatus());
        order.setUser(userRepository.findById(cart.getUser().getId()).orElseThrow());

        BigDecimal tp = orderItems.stream()
                        .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal_price(tp);
        return orderRepository.save(order);
    }

    public Order createOrder(OrderDto orderDto) throws AccessDeniedException {
        Order order = prepareOrder(orderDto);

        if (order.getUser().isBlocked()) throw new AccessDeniedException(
                String.format("Вы заблокированы. Причина: %s", order.getUser().getBlockedReason()));

        order.setStatus(OrderStatus.PENDING_APPROVAL.getStatus());
        return orderRepository.save(order);}

    private Order prepareOrder(OrderDto orderDto) {
        Long userId = orderDto.getUserId();
        User user = userRepository.findById(userId).orElseThrow();
        Order order = orderMapper.orderDtoToOrder(orderDto);
        order.setUser(user);
        return order;
    }

    public Order addItemToOrder(Long orderId, Product product, int quantity) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        Optional<OrderItem> byProduct = order.getOrderItems().stream()
                .filter(oi -> oi.getProduct().equals(product))
                .findFirst();

        if (byProduct.isPresent()) throw new IllegalArgumentException("Product already exists");

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setOrder(order);
        orderItem.setPrice(product.getPrice().multiply(new BigDecimal(quantity)));
        OrderItem newItem = orderItemService.addOrderItem(orderItem);
        order.getOrderItems().add(newItem);

        return orderRepository.save(order);
    }

    public Order cancelOrder(Long orderId, User currentUser) throws AccessDeniedException {
        return this.setOrderStatus(orderId, OrderStatus.CANCELLED, currentUser);
    }

    public Order setOrderStatus(Long orderId, OrderStatus status, User currentUser) throws AccessDeniedException {
        Order order = orderRepository.findById(orderId).orElseThrow();

        if (status == OrderStatus.ACTIVE && !Objects.equals(currentUser.getRole(), Roles.ROLE_ADMIN.getRole()))
            throw new AccessDeniedException("Только админ может активировать заказ.");

        if (status == OrderStatus.CANCELLED &&
                (!currentUser.getRole().equals(Roles.ROLE_ADMIN.getRole()) && !currentUser.getId().equals(order.getUser().getId())))
            throw new AccessDeniedException("Только админ или владелец заказа может отменить заказ.");

        if (status == OrderStatus.PENDING_APPROVAL && order.getStatus() != null)
            throw new IllegalArgumentException("Нельзя установить этот статус %s".formatted(status.getStatus()));

        order.setStatus(status.getStatus());
        return orderRepository.save(order);
    }
}
