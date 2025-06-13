package org.practice._3d_prin_shop.repository;

import org.practice._3d_prin_shop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
