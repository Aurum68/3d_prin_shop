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

    public Product getProductById(Integer id) {
        return this.productRepository.getProductById(id);
    }

    public Product saveProduct(Product product) {
        try {
            return this.productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Product updateProduct(Integer id, Product product) {
        Product oldProduct = this.productRepository.getProductById(id);

        if (oldProduct == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        oldProduct.setName(product.getName());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setCategory(product.getCategory());
        oldProduct.setStock_quantity(product.getStock_quantity());
        oldProduct.setImage(product.getImage());

        return this.productRepository.save(oldProduct);
    }

    public void deleteProduct(Integer id) {
        this.productRepository.deleteById(id);
    }

}
