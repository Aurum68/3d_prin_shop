package org.practice._3d_prin_shop.rest_controller;

import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasRole('admin')")
public class AdminProductRestController {

    private final ProductService productService;

    @Autowired
    public AdminProductRestController(ProductService productService) {this.productService = productService;}

    @PostMapping
    public Product addProduct(@RequestBody Product product) {return this.productService.addProduct(product);}

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return this.productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {this.productService.deleteProduct(id);}
}
