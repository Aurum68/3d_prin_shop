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
import java.util.Arrays;
import java.util.List;

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
    void testGetAllUsers() throws Exception {
        List<User> users = getUsers();

        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setUsername("Bob");
        userDto1.setPassword("BobPassword");
        userDto1.setEmail("Bob@gmail.com");

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setUsername("Bill");
        userDto2.setPassword("BillPassword");
        userDto2.setEmail("Bill@gmail.com");

        List<UserDto> userDtoList = Arrays.asList(userDto1, userDto2);

        Mockito.when(userService.getAllUsers()).thenReturn(users);
        Mockito.when(userMapper.toDtoList(users)).thenReturn(userDtoList);

        mockMvc.perform(get("/api/user/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("Bob"))
                .andExpect(jsonPath("$[0].password").value("BobPassword"))
                .andExpect(jsonPath("$[0].email").value("Bob@gmail.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("Bill"))
                .andExpect(jsonPath("$[1].password").value("BillPassword"))
                .andExpect(jsonPath("$[1].email").value("Bill@gmail.com"));

        Mockito.verify(userService).getAllUsers();
        Mockito.verify(userMapper).toDtoList(users);
    }

    private static List<User> getUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Bob");
        user1.setPassword("BobPassword");
        user1.setEmail("Bob@gmail.com");
        user1.setRole(Roles.ROLE_USER.getRole());

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("Bill");
        user2.setPassword("BillPassword");
        user2.setEmail("Bill@gmail.com");
        user2.setRole(Roles.ROLE_USER.getRole());

        return Arrays.asList(user1, user2);
    }

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
