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
}
