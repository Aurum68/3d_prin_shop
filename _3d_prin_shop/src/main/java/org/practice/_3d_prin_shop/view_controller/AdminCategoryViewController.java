package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.model.Category;
import org.practice._3d_prin_shop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryViewController {

    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryViewController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/categories";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("categories", new Category());
        return "admin/category-add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("categories") Category category, RedirectAttributes redirectAttributes) {
        Category addedCategory = categoryService.addCategory(category);
        redirectAttributes.addFlashAttribute("category", addedCategory);
        return "redirect:/admin/category/all";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) return "error/404";

        model.addAttribute("category", category);
        return "admin/category-edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @ModelAttribute  Category category, RedirectAttributes redirectAttributes) {
        Category updated = categoryService.renameCategory(id, category.getName());
        redirectAttributes.addFlashAttribute("category", updated);
        return "redirect:/admin/category/all";
    }
}
