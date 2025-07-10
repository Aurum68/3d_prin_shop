package org.practice._3d_prin_shop.service;

import org.practice._3d_prin_shop.model.OrderItem;
import org.practice._3d_prin_shop.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItem getOrderItemById(Long id) {return orderItemRepository.findById(id).orElseThrow();}

    public OrderItem addOrderItem(OrderItem orderItem) {return orderItemRepository.save(orderItem);}

    public void deleteOrderItem(OrderItem orderItem) {orderItemRepository.delete(orderItem);}
}
