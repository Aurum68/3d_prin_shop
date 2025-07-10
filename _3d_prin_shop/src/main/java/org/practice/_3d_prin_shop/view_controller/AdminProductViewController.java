package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.dto.ProductDto;
import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.service.CategoryService;
import org.practice._3d_prin_shop.service.ProductService;
import org.practice._3d_prin_shop.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/products")
@PreAuthorize("hasRole('admin')")
public class AdminProductViewController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    @Autowired
    public AdminProductViewController(ProductService productService, ProductMapper productMapper, CategoryService categoryService) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String adminProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/{id}")
    public String adminProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) return "error/404";

        model.addAttribute("product", product);
        return "admin/product";
    }

    @GetMapping("/add")
    public String adminProductAdd(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product-add";
    }

    @PostMapping("/add")
    public String adminProductAdd(@ModelAttribute("product") ProductDto product, RedirectAttributes redirectAttributes) throws IOException {
        String image_url = saveImage(product.getImage_url());

        Product newProduct = productMapper.productDtoToProduct(product);
        newProduct.setImage_url(image_url);

        productService.addProduct(newProduct);
        redirectAttributes.addFlashAttribute("success", "Product added successfully");
        return "redirect:/admin/products/";
    }

    @GetMapping("/{id}/edit")
    public String adminProductEdit(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) return "error/404";

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product-edit";
    }

    @PostMapping("/{id}/edit")
    public String adminProductEdit(@PathVariable Long id,
                                   @ModelAttribute("product") ProductDto product,
                                   RedirectAttributes redirectAttributes) {

        MultipartFile file = product.getImage_url();

        Product oldProduct = productService.getProductById(id);
        Product newProduct = productMapper.productDtoToProduct(product);
        if (newProduct == null) return "error/404";

        if (file != null && !file.isEmpty()) {
            try {
                String newFileName = saveImage(file);
                newProduct.setImage_url(newFileName);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Error while saving image");
                return "redirect:/admin/products/" + id;
            }
        }else {
            newProduct.setImage_url(oldProduct.getImage_url());
        }
        productService.updateProduct(id, newProduct);
        redirectAttributes.addFlashAttribute("success", "Product updated successfully");
        return "redirect:/admin/products/";
    }

    private String saveImage(MultipartFile image) throws IOException {
        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path uploadPath = Paths.get("uploads/images/");
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(filename);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/images/" + filename;
    }

    @PostMapping("/{id}/delete")
    public String adminProductDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("success", "Product deleted successfully");
        return "redirect:/admin/products";
    }
}
