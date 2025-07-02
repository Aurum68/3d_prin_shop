package org.practice._3d_prin_shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.practice._3d_prin_shop.model.OrderItem;
import org.practice._3d_prin_shop.repository.OrderItemRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTests {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemService orderItemService;

    @Test
    void testGetOrderItemById() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);

        Mockito.when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));

        OrderItem result = orderItemService.getOrderItemById(1L);

        Assertions.assertEquals(orderItem, result);
        Mockito.verify(orderItemRepository).findById(1L);
    }

    @Test
    void testAddOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);

        Mockito.when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

        OrderItem result = orderItemService.addOrderItem(orderItem);

        Assertions.assertEquals(orderItem, result);
        Mockito.verify(orderItemRepository).save(orderItem);
    }

    @Test
    void testDeleteOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);

        orderItemService.deleteOrderItem(orderItem);
        Mockito.verify(orderItemRepository).delete(orderItem);
    }
}
