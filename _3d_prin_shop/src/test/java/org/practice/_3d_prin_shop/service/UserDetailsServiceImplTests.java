package org.practice._3d_prin_shop.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.repository.UserRepository;
import org.practice._3d_prin_shop.util.Roles;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void testLoadUserByUsername_success() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setRole(Roles.ROLE_USER.getRole());

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        Assertions.assertEquals(user.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(user.getPassword(), userDetails.getPassword());
        Assertions.assertTrue(userDetails.getAuthorities().stream()
                .allMatch(role -> role.getAuthority().equals(Roles.ROLE_USER.getRole())));

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(user.getUsername());
    }

    @Test
    void testLoadUserByUsername_failure() {
        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("username"));
    }
}
