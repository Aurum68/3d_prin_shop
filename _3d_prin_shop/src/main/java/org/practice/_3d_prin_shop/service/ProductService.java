package org.practice._3d_prin_shop.service;

import org.practice._3d_prin_shop.model.Product;
import org.practice._3d_prin_shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return this.productRepository.getProductById(id);
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.searchProducts(null, categoryId);
    }

    public List<Product> searchProducts(String keyword, Long categoryId) {
        String txt = (keyword == null || keyword.isEmpty()) ? null : keyword;
        return productRepository.searchProducts(txt, categoryId);
    }

    public Product addProduct(Product product) {
        try {
            return this.productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Product updateProduct(Long id, Product product) {
        Product oldProduct = this.productRepository.getProductById(id);

        if (oldProduct == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        oldProduct.setName(product.getName());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setCategory(product.getCategory());
        oldProduct.setStock(product.getStock());
        oldProduct.setImage_url(product.getImage_url());

        return this.productRepository.save(oldProduct);
    }

    public void deleteProduct(Long id) {this.productRepository.deleteProductById(id);}
}
