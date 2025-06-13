package org.practice._3d_prin_shop.service;

import org.practice._3d_prin_shop.model.Order;
import org.practice._3d_prin_shop.model.OrderItem;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.repository.OrderRepository;
import org.practice._3d_prin_shop.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
    }

    public List<Order> findAll() {return orderRepository.findAll();}

    public List<Order> findByUserId(Long userId) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public Order getOrder(Long id) {return orderRepository.findById(id).orElseThrow();}

    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.PENDING_APPROVAL.getStatus());
        return orderRepository.save(order);}

    public Order addItemToOrder(Long orderId, Product product, int quantity) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        Optional<OrderItem> byProduct = order.getOrderItems().stream()
                .filter(oi -> oi.getProduct().equals(product))
                .findFirst();

        if (byProduct.isEmpty()) {

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setOrder(order);
            orderItem.setPrice(product.getPrice().multiply(new BigDecimal(quantity)));
            orderItemService.addOrderItem(orderItem);
            order.getOrderItems().add(orderItem);
            orderRepository.save(order);
        }
        return order;
    }

    public Order updateOrder(Long orderId, Order order) {
        Order orderToUpdate = orderRepository.findById(orderId).orElseThrow();
        orderToUpdate.setStatus(order.getStatus());
        return orderRepository.save(orderToUpdate);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.CANCELLED.getStatus());
        orderRepository.save(order);
    }
}
