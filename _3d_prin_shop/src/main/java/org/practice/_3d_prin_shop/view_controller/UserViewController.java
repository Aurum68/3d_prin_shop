package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.dto.UserDto;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user-profile")
public class UserViewController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserViewController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public String userProfile(@PathVariable Long id, Model model) {
        UserDto user = userMapper.userToUserDto(userService.getUserById(id));
        if(user == null) return "error/404";

        model.addAttribute("user", user);
        return "user-profile";
    }

    @GetMapping("/{id}/edit")
    public String userProfileEdit(@PathVariable Long id, Model model) {
        UserDto user = userMapper.userToUserDto(userService.getUserById(id));
        if(user == null) return "error/404";

        model.addAttribute("user", user);
        return "user-edit";
    }

    @PostMapping("/{id}/edit")
    public String userProfileEditSubmit(@PathVariable Long id,
                                        @ModelAttribute("user") UserDto user,
                                        RedirectAttributes redirectAttributes) {

        User userToUpdate = userMapper.userDtoToUser(user);
        userService.updateUserById(id, userToUpdate);
        redirectAttributes.addFlashAttribute("success", "User profile updated");

        return "redirect:/user-profile/" + id;
    }
}
