package org.practice._3d_prin_shop.rest_controller;

import org.practice._3d_prin_shop.dto.ProductDto;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.service.ProductService;
import org.practice._3d_prin_shop.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductRestController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return productMapper.productsToProductDtos(products);
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        return productMapper.productToProductDto(productService.getProductById(id));
    }
}
