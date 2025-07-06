package org.practice._3d_prin_shop.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.practice._3d_prin_shop.dto.UserDto;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.util.Roles;
import org.practice._3d_prin_shop.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class UserMapperTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testUserToUserDto(){
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBlocked(false);
        user.setRole(Roles.ROLE_USER.getRole());

        UserDto userDto = userMapper.userToUserDto(user);

        Assertions.assertEquals(user.getId(), userDto.getId());
        Assertions.assertEquals(user.getUsername(), userDto.getUsername());
        Assertions.assertEquals(user.getPassword(), userDto.getPassword());
        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
        Assertions.assertEquals(user.getFirstName(), userDto.getFirstName());
        Assertions.assertEquals(user.getLastName(), userDto.getLastName());
    }

    @Test
    void testUserDtoToUser(){
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("username");
        userDto.setPassword("password");
        userDto.setEmail("email@email.com");
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");

        User user = userMapper.userDtoToUser(userDto);

        Assertions.assertEquals(user.getId(), userDto.getId());
        Assertions.assertEquals(user.getUsername(), userDto.getUsername());
        Assertions.assertEquals(user.getPassword(), userDto.getPassword());
        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
        Assertions.assertEquals(user.getFirstName(), userDto.getFirstName());
        Assertions.assertEquals(user.getLastName(), userDto.getLastName());
    }

    @Test
    void testToDtoList(){
        User user = new User();
        user.setId(1L);

        User user1 = new User();
        user1.setId(2L);

        List<User> users = Arrays.asList(user, user1);

        List<UserDto> userDtos = userMapper.toDtoList(users);

        Assertions.assertEquals(userDtos.size(), users.size());
        Assertions.assertEquals(user.getId(), userDtos.get(0).getId());
        Assertions.assertEquals(user1.getId(), userDtos.get(1).getId());
    }
}
