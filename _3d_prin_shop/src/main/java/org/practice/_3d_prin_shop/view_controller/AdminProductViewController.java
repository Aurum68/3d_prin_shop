package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
@PreAuthorize("hasRole('admin')")
public class AdminProductViewController {

    private final ProductService productService;

    @Autowired
    public AdminProductViewController(ProductService productService) {this.productService = productService;}

    @GetMapping("/")
    public String adminProducts(Model model) {
        model.addAttribute("admin_products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/{id}")
    public String adminProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) return "error/404";

        model.addAttribute("admin_product", product);
        return "admin/product";
    }

    @GetMapping("/{id}/edit")
    public String adminProductEdit(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) return "error/404";

        model.addAttribute("admin_product", product);
        return "admin/product-edit";
    }

    @PostMapping("/{id}/edit")
    public String adminProductEdit(@PathVariable Long id,
                                   @ModelAttribute("product") Product product,
                                   RedirectAttributes redirectAttributes) {
        productService.updateProduct(id, product);
        redirectAttributes.addFlashAttribute("success", "Product updated successfully");
        return "redirect:/admin/products";
    }

    @GetMapping("/add")
    public String adminProductAdd(Model model) {
        model.addAttribute("admin_product", new Product());
        return "admin/product-add";
    }

    @PostMapping("/add")
    public String adminProductAdd(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) {
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("success", "Product added successfully");
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String adminProductDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("success", "Product deleted successfully");
        return "redirect:/admin/products";
    }
}
