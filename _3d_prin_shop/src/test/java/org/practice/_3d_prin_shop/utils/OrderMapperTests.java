package org.practice._3d_prin_shop.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.practice._3d_prin_shop.dto.OrderDto;
import org.practice._3d_prin_shop.model.Order;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.util.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class OrderMapperTests {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void testOrderToOrderDto(){
        User user = new User();

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);

        OrderDto orderDto = orderMapper.orderToOrderDto(order);

        Assertions.assertEquals(order.getId(), orderDto.getId());
        Assertions.assertEquals(order.getUser().getId(), orderDto.getUserId());
    }

    @Test
    void testOrderDtoToOrder(){
        User user = new User();

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(user.getId());

        Order order = orderMapper.orderDtoToOrder(orderDto);

        Assertions.assertEquals(order.getId(), orderDto.getId());
        Assertions.assertEquals(order.getUser().getId(), orderDto.getUserId());
    }

    @Test
    void testToDtoList(){
        Order order = new Order();
        order.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);

        List<Order> orders = Arrays.asList(order, order2);

        List<OrderDto> orderDtos = orderMapper.toDtoList(orders);

        Assertions.assertEquals(order.getId(), orderDtos.get(0).getId());
        Assertions.assertEquals(order2.getId(), orderDtos.get(1).getId());

    }
}
