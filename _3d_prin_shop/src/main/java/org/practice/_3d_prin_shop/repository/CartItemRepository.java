package org.practice._3d_prin_shop.repository;

import org.practice._3d_prin_shop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {}
