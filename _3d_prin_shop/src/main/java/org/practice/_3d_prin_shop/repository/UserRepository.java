package org.practice._3d_prin_shop.repository;

import org.practice._3d_prin_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserById(Long id);

    Optional<User> findByUsername(String username);
}
