package org.practice._3d_prin_shop.view;

import org.practice._3d_prin_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductViewController {

    private final ProductService productService;

    @Autowired
    public ProductViewController(ProductService productService) {this.productService = productService;}

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }
}
