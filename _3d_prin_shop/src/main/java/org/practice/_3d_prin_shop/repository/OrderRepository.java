package org.practice._3d_prin_shop.repository;

import org.practice._3d_prin_shop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findByStatus(String status);
}
