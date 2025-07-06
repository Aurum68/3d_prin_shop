package org.practice._3d_prin_shop.rest_controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {
    private final OrderService orderService;
    private final UserService userService;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderRestController(OrderService orderService, UserService userService, ProductMapper productMapper, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.userService = userService;
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
    }

    @GetMapping("/orders_of{userId}")
    public List<OrderDto> getOrdersOfUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getByUserId(userId);
        return orderMapper.toDtoList(orders);
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return orderMapper.orderToOrderDto(order);
    }

    @PostMapping("/add")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        try {
            Order result = orderService.createOrder(orderDto);
            OrderDto orderDtoResult = orderMapper.orderToOrderDto(result);
            return ResponseEntity.ok(orderDtoResult);
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/{orderId}/add")
    public OrderDto addItemToOrder(@PathVariable Long orderId, @RequestBody AddToOrderRequest request) {
        ProductDto dto = request.getProduct();
        Product product = productMapper.productDtoToProduct(dto);
        Order order = orderService.addItemToOrder(orderId, product, request.getQuantity());
        return orderMapper.orderToOrderDto(order);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> setOrderStatus(@PathVariable Long orderId, @RequestParam String status,
                                   @AuthenticationPrincipal UserPrincipal userPrincipal) {
        OrderStatus orderStatus = OrderStatus.fromString(status);

        User user = this.userService.getUserByUsername(userPrincipal.getUsername());
        try {
            Order order = orderService.setOrderStatus(orderId, orderStatus, user);
            return ResponseEntity.ok(orderMapper.orderToOrderDto(order));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = this.userService.getUserByUsername(userPrincipal.getUsername());
        try {
            Order order = orderService.cancelOrder(orderId, user);
            return ResponseEntity.ok(orderMapper.orderToOrderDto(order));
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }
}
