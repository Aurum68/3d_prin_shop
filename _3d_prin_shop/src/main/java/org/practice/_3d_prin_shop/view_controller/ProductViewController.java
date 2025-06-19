package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.dto.ProductDto;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.service.ProductService;
import org.practice._3d_prin_shop.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductViewController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping("/")
    public String products(Model model) {
        model.addAttribute("products", productMapper.productsToProductDtos(productService.getAllProducts()));
        return "products";
    }

    @GetMapping("/{id}")
    public String product(@PathVariable Long id, Model model) {
        ProductDto product = productMapper.productToProductDto(productService.getProductById(id));
        if (product == null) return "error/404";

        model.addAttribute("product", product);
        return "product-card";
    }
}
