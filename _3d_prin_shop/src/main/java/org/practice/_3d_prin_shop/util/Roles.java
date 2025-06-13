package org.practice._3d_prin_shop.util;

import lombok.Getter;

@Getter
public enum Roles {
    ROLE_ADMIN ("admin"),
    ROLE_USER ("user");

    private final String role;
    Roles(String role) {this.role = role;}
}
