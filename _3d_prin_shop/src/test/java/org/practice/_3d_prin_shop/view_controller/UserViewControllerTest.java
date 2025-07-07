package org.practice._3d_prin_shop.view_controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.practice._3d_prin_shop.dto.UserDto;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(controllers = UserViewController.class)
public class UserViewControllerTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testUserProfile_success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("Bob");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("Bob");

        Mockito.when(userService.getUserById(1L)).thenReturn(user);
        Mockito.when(userMapper.userToUserDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/user-profile/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("account"))
                .andExpect(model().attribute("user", userDto));

        Mockito.verify(userService).getUserById(1L);
        Mockito.verify(userMapper).userToUserDto(user);
    }
}
