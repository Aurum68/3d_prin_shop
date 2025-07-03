package org.practice._3d_prin_shop.rest_controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = AdminUserRestController.class)
public class AdminUserRestControllerTests {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

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
        blockedUser.setBlacklisted(true);
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
        unblockedUser.setBlacklisted(false);

        Mockito.when(userService.unblockUserById(1L)).thenReturn(unblockedUser);

        mockMvc.perform(post("/api/admin/user/{id}/unblock", 1L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.blacklisted").value(false));
    }
}
