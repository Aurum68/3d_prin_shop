package org.practice._3d_prin_shop.rest_controller;

import org.practice._3d_prin_shop.dto.UserDto;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return userMapper.toDtoList(users);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userMapper.userToUserDto(this.userService.getUserById(id));
    }

    @PostMapping("/add")
    public UserDto createUser(@RequestBody UserDto user) {
        User newUser = userMapper.userDtoToUser(user);
        UserDto userDto = userMapper.userToUserDto(this.userService.addUser(newUser));
        System.err.println(userDto);
        return userDto;
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto user) {
        User userUpdates = userMapper.userDtoToUser(user);
        User updatedUser = this.userService.updateUserById(id, userUpdates);
        System.err.println(updatedUser);
        return userMapper.userToUserDto(updatedUser);
    }
}
