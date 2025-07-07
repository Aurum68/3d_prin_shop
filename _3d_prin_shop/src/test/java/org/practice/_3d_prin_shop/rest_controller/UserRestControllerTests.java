package org.practice._3d_prin_shop.rest_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.practice._3d_prin_shop.dto.UserDto;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.Roles;
import org.practice._3d_prin_shop.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testGetUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("Bob");
        user.setPassword("password");
        user.setEmail("Bob@gmail.com");
        user.setRole(Roles.ROLE_USER.getRole());

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("Bob");
        userDto.setPassword("password");
        userDto.setEmail("Bob@gmail.com");

        Mockito.when(userService.getUserById(1L)).thenReturn(user);
        Mockito.when(userMapper.userToUserDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/api/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("Bob"));

        Mockito.verify(userService).getUserById(1L);
        Mockito.verify(userMapper).userToUserDto(user);
    }

    @Test
    void testCreateUser() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(1L);
        user.setUsername("Bob");
        user.setPassword("password");
        user.setEmail("Bob@gmail.com");
        user.setRole(Roles.ROLE_USER.getRole());
        user.setCreated_at(now);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("Bob");
        userDto.setPassword("password");
        userDto.setEmail("Bob@gmail.com");

        Mockito.when(userMapper.userDtoToUser(Mockito.any(UserDto.class))).thenReturn(user);
        Mockito.when(userService.addUser(user)).thenReturn(user);
        Mockito.when(userMapper.userToUserDto(Mockito.any(User.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/user/add")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("Bob"))
                .andExpect(jsonPath("$.password").value("password"))
                .andExpect(jsonPath("$.email").value("Bob@gmail.com"));

        Mockito.verify(userMapper).userDtoToUser(Mockito.any(UserDto.class));
        Mockito.verify(userService).addUser(user);
        Mockito.verify(userMapper).userToUserDto(Mockito.any(User.class));
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testUpdateUser() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(1L);
        user.setUsername("Bob");
        user.setPassword("password");
        user.setEmail("Bob@gmail.com");
        user.setRole(Roles.ROLE_USER.getRole());
        user.setCreated_at(now);

        UserDto userDtoUpdate = new UserDto();
        userDtoUpdate.setId(1L);
        userDtoUpdate.setUsername("Bob");
        userDtoUpdate.setPassword("newPassword");
        userDtoUpdate.setEmail("Bob@gmail.com");

        User userUpdate = new User();
        userUpdate.setUsername("Bob");
        userUpdate.setPassword("newPassword");
        userUpdate.setEmail("Bob@gmail.com");

        Mockito.when(userMapper.userDtoToUser(Mockito.any(UserDto.class))).thenReturn(userUpdate);
        Mockito.when(userService.updateUserById(1L, userUpdate)).thenReturn(user);
        Mockito.when(userMapper.userToUserDto(Mockito.any(User.class))).thenReturn(userDtoUpdate);

        mockMvc.perform(put("/api/user/{id}", 1L)
                .content(objectMapper.writeValueAsString(userDtoUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("Bob"))
                .andExpect(jsonPath("$.password").value("newPassword"))
                .andExpect(jsonPath("$.email").value("Bob@gmail.com"));

        Mockito.verify(userMapper).userDtoToUser(Mockito.any(UserDto.class));
        Mockito.verify(userService).updateUserById(1L, userUpdate);
        Mockito.verify(userMapper).userToUserDto(Mockito.any(User.class));
    }
}
