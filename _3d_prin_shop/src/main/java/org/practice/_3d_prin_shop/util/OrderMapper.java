package org.practice._3d_prin_shop.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.practice._3d_prin_shop.dto.OrderDto;
import org.practice._3d_prin_shop.model.Order;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "id", target = "id")
    OrderDto orderToOrderDto(Order order);
    Order orderDtoToOrder(OrderDto dto);
    List<OrderDto> toDtoList(List<Order> orders);
}
