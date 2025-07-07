package org.practice._3d_prin_shop.rest_controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = AdminUserRestController.class)
public class AdminUserRestControllerTests {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "Bob", roles = {"user"})
    void testGetAllUsers() throws Exception {
        List<User> users = getUsers();

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/admin/user/all"))
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
    @WithMockUser(username = "admin", roles = {"admin"})
    public void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUserById(1L);

        mockMvc.perform(delete("/api/admin/user/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUserById(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"admin"})
    void testBlockUser() throws Exception {
        String reason = "reason";

        User blockedUser = new User();
        blockedUser.setId(1L);
        blockedUser.setBlocked(true);
        blockedUser.setBlockedReason(reason);

        Mockito.when(userService.blockUserById(1L, reason)).thenReturn(blockedUser);

        mockMvc.perform(post("/api/admin/user/{id}/block", 1L)
                .content(reason)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.blacklisted").value(true))
                .andExpect(jsonPath("$.blockedReason").value(reason));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"admin"})
    void testUnblockUser() throws Exception {
        User unblockedUser = new User();
        unblockedUser.setId(1L);
        unblockedUser.setBlocked(false);

        Mockito.when(userService.unblockUserById(1L)).thenReturn(unblockedUser);

        mockMvc.perform(post("/api/admin/user/{id}/unblock", 1L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.blacklisted").value(false));
    }
}
