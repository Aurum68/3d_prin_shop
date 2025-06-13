package org.practice._3d_prin_shop.util;

import org.mapstruct.Mapper;
import org.practice._3d_prin_shop.dto.OrderDto;
import org.practice._3d_prin_shop.model.Order;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto orderToOrderDto(Order order);
    Order orderDtoToOrder(OrderDto dto);
    List<OrderDto> ordersToOrderDtos(List<Order> orders);
}
