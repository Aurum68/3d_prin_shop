package org.practice._3d_prin_shop.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.practice._3d_prin_shop.model.OrderItem;
import org.practice._3d_prin_shop.model.User;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private User user;
    private List<OrderItem> orderItems;

    @Setter(AccessLevel.NONE)
    private String status;

    private BigDecimal total_price;
}
