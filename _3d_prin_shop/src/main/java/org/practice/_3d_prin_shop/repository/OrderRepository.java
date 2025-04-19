package org.practice._3d_prin_shop.repository;

import org.practice._3d_prin_shop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {}
