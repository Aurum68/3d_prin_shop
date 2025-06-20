package org.practice._3d_prin_shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String image_url;
}
