package org.practice._3d_prin_shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.practice._3d_prin_shop.model.Cart;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.repository.CartRepository;
import org.practice._3d_prin_shop.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        List<User> users = Arrays.asList(user1, user2);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();
        Assertions.assertEquals(users, result);
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void testGetUserById() {
        User user1 = new User();
        user1.setId(1L);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User result = userService.getUserById(1L);
        Assertions.assertEquals(user1, result);
        Mockito.verify(userRepository).findById(1L);
    }

    @Test
    void testAddUser() {
        User userToAdd = new User();
        userToAdd.setId(1L);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userToAdd);

        User result = userService.addUser(userToAdd);

        Assertions.assertEquals(userToAdd, result);
        Mockito.verify(userRepository).save(userToAdd);
        Mockito.verify(cartRepository).save(Mockito.any(Cart.class));
    }

    @Test
    void testUpdateUser() {
        User existing = new User();
        existing.setId(5L);

        User update = new User();
        update.setUsername("update");
        update.setPassword("password");
        update.setEmail("update@email.com");

        Mockito.when(userRepository.findById(5L)).thenReturn(Optional.of(existing));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(existing);

        User result = userService.updateUserById(5L, update);

        Assertions.assertEquals(result.getUsername(), update.getUsername());
        Assertions.assertEquals(result.getPassword(), update.getPassword());
        Assertions.assertEquals(result.getEmail(), update.getEmail());

        Mockito.verify(userRepository).findById(5L);
        Mockito.verify(userRepository).save(existing);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUserById(1L);
        Mockito.verify(userRepository).deleteById(1L);
    }
}
