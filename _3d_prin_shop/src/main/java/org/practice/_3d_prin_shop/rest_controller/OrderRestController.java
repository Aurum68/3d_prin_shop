package org.practice._3d_prin_shop.rest_controller;

import org.practice._3d_prin_shop.dto.OrderDto;
import org.practice._3d_prin_shop.dto.ProductDto;
import org.practice._3d_prin_shop.model.Order;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.service.OrderService;
import org.practice._3d_prin_shop.util.OrderMapper;
import org.practice._3d_prin_shop.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {
    private final OrderService orderService;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderRestController(OrderService orderService, ProductMapper productMapper, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
    }

    @GetMapping("/orders_of{userId}")
    public List<OrderDto> getOrdersOfUser(@PathVariable Long userId) {
        return orderMapper.ordersToOrderDtos(orderService.findByUserId(userId));
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable Long orderId) {
        return orderMapper.orderToOrderDto(orderService.getOrder(orderId));
    }

    @PostMapping("/add")
    public OrderDto createOrder(@RequestBody OrderDto orderDto) {
        Order order = orderMapper.orderDtoToOrder(orderDto);
        return orderMapper.orderToOrderDto(orderService.createOrder(order));
    }

    @PostMapping("/{orderId}/add")
    public OrderDto addItemToOrder(@PathVariable Long orderId, @RequestBody ProductDto productDto, @RequestBody int quantity) {
        Product product = productMapper.productDtoToProduct(productDto);
        return orderMapper.orderToOrderDto(orderService.addItemToOrder(orderId, product, quantity));
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {orderService.cancelOrder(orderId);}
}
