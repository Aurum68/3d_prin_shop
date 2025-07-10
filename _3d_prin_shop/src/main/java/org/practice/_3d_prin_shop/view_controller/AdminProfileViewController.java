package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.dto.UserDto;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.security.UserPrincipal;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.Roles;
import org.practice._3d_prin_shop.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/profile")
public class AdminProfileViewController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public AdminProfileViewController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String profile(Model model, @AuthenticationPrincipal UserPrincipal user) {
        if (!user.getRole().equals(Roles.ROLE_ADMIN.getRole())) return "error/403";

        User userDetails = userService.getUserByUsername(user.getUsername());
        model.addAttribute("user", userMapper.userToUserDto(userDetails));
        return "admin/profile";
    }

    @GetMapping("/edit")
    public String edit(Model model, @AuthenticationPrincipal UserPrincipal user) {
        if (!user.getRole().equals(Roles.ROLE_ADMIN.getRole())) return "error/403";
        User userDetails = userService.getUserByUsername(user.getUsername());
        model.addAttribute("user", userMapper.userToUserDto(userDetails));
        return "admin/edit-profile";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("user") UserDto userDto, RedirectAttributes redirectAttributes) {
        User userToUpdate = userService.getUserById(userDto.getId());
        User updatedUser = userService.updateUserById(userDto.getId(), userToUpdate);
        redirectAttributes.addFlashAttribute("user", userMapper.userToUserDto(updatedUser));
        return "redirect:/admin/profile";
    }
}
