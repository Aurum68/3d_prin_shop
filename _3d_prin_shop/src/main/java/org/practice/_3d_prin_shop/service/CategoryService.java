package org.practice._3d_prin_shop.service;

import org.practice._3d_prin_shop.model.Category;
import org.practice._3d_prin_shop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category renameCategory(Long id, String categoryNane) {
        Category updatedCategory = categoryRepository.findById(id).orElseThrow();
        updatedCategory.setName(categoryNane);
        return categoryRepository.save(updatedCategory);
    }
}
