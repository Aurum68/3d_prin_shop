package org.practice._3d_prin_shop.repository;

import org.practice._3d_prin_shop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {}
