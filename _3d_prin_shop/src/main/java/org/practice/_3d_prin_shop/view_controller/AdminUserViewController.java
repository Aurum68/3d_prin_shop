package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.dto.UserDto;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('admin')")
public class AdminUserViewController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public AdminUserViewController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/all")
    public String allUsers(Model model) {
        List<UserDto> dtoList = userMapper.toDtoList(userService.getAllUsers());
        model.addAttribute("users", dtoList);
        return "admin/users";
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable Long id, Model model) {
        UserDto userDto = userMapper.userToUserDto(userService.getUserById(id));
        model.addAttribute("user", userDto);
        return "admin/user";
    }

    @PostMapping("/{id}/block")
    public String blockUser(@PathVariable Long id, @RequestBody String reason, RedirectAttributes attributes) {
        User user = userService.blockUserById(id, reason);
        attributes.addFlashAttribute("user", user);
        return "redirect:/admin/users/" + id;
    }

    @PostMapping("/{id}/unblock")
    public String unblockUser(@PathVariable Long id, RedirectAttributes attributes) {
        User user = userService.unblockUserById(id);
        attributes.addFlashAttribute("user", user);
        return "redirect:/admin/users/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes attributes) {
        userService.deleteUserById(id);
        attributes.addFlashAttribute("success", "User " + id + " has been deleted");
        return "redirect:/admin/users/all";
    }

}
