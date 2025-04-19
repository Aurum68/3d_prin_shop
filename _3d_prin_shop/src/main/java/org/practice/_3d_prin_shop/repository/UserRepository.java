package org.practice._3d_prin_shop.repository;

import org.practice._3d_prin_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}
