package org.practice._3d_prin_shop.repository;

import org.practice._3d_prin_shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product getProductById(Long id);

    void deleteProductById(Long id);
}
