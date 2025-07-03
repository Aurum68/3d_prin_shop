package org.practice._3d_prin_shop.rest_controller;

import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
@PreAuthorize("hasRole('admin')")
public class AdminUserRestController {

    private final UserService userService;

    @Autowired
    public AdminUserRestController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        this.userService.deleteUserById(id);
    }

    @PostMapping("/{id}/block")
    public User blockUser(@PathVariable Long id, @RequestBody String reason) {
        return this.userService.blockUserById(id, reason);
    }

    @PostMapping("/{id}/unblock")
    public User unblockUser(@PathVariable Long id) {
        return this.userService.unblockUserById(id);
    }
}
