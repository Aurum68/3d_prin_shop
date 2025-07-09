package org.practice._3d_prin_shop.service;

import org.practice._3d_prin_shop.model.Cart;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.repository.CartRepository;
import org.practice._3d_prin_shop.repository.UserRepository;
import org.practice._3d_prin_shop.util.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public UserService(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public List<User> getAllUsers() {return this.userRepository.findAll();}

    public Optional<List<User>> getBlockedUsers() {
        return userRepository.findByBlocked(true);
    }

    public User getUserById(Long userId) {return this.userRepository.findById(userId).orElseThrow();}

    public User getUserByUsername(String username) {return userRepository.findByUsername(username).orElseThrow();}

    public User addUser(User user) {
        user.setRole(Roles.ROLE_USER.getRole());
        user.setBlocked(false);
        user.setCreated_at(LocalDateTime.now());
        this.userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(user);
        this.cartRepository.save(cart);

        user.setCart(cart);

        this.userRepository.save(user);
        minLog(this.getUserByUsername(user.getUsername()));
        return user;
    }

    private void minLog(User user) {
        System.err.println(user.getEmail());
        System.err.println(user.getPassword());
    }

    public User updateUserById(Long id, User user) {
        User u = this.userRepository.findById(id).orElseThrow();

        u.setUsername(user.getUsername());
        u.setPassword(user.getPassword());
        u.setEmail(user.getEmail());
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());

        return this.userRepository.save(u);
    }

    public User blockUserById(Long id, String reason) {
        User u = this.userRepository.findById(id).orElseThrow();
        u.setBlocked(true);
        u.setBlockedReason(reason);
        return this.userRepository.save(u);
    }

    public User unblockUserById(Long id) {
        User u = this.userRepository.findById(id).orElseThrow();
        u.setBlocked(false);
        u.setBlockedReason(null);
        return this.userRepository.save(u);
    }

    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }
}
