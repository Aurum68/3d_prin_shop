package org.practice._3d_prin_shop.rest_controller;

import org.practice._3d_prin_shop.dto.UserDto;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userMapper.userToUserDto(this.userService.getUserById(id));
    }

    @PostMapping("/add")
    public UserDto createUser(@RequestBody UserDto user) {

        User newUser = userMapper.userDtoToUser(user);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(encodedPassword);

        return userMapper.userToUserDto(this.userService.addUser(newUser));
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto user) {
        User existingUser = this.userService.getUserById(id);

        String rawPassword = user.getPassword();
        User userUpdates = userMapper.userDtoToUser(user);

        if (rawPassword == null || rawPassword.isEmpty()) {
            userUpdates.setPassword(existingUser.getPassword());
        }
        else {
            userUpdates.setPassword(passwordEncoder.encode(rawPassword));
        }

        User updatedUser = this.userService.updateUserById(id, userUpdates);
        return userMapper.userToUserDto(updatedUser);
    }
}
