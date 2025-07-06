package org.practice._3d_prin_shop.util;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING_APPROVAL ("pending_approval"),
    ACTIVE ("active"),
    CANCELLED ("cancelled");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public static OrderStatus fromString(String status) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getStatus().equalsIgnoreCase(status)) return orderStatus;
        }

        throw new IllegalArgumentException("Invalid order status: " + status);
    }
}
