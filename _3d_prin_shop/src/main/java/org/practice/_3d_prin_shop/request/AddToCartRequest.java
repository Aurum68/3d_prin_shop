package org.practice._3d_prin_shop.request;

import lombok.Getter;
import lombok.Setter;
import org.practice._3d_prin_shop.dto.ProductDto;

@Getter
@Setter
public class AddToCartRequest {
    private Long id;
    private ProductDto product;
    private int quantity;
}
