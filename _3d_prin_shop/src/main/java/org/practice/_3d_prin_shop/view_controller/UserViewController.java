package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.dto.UserDto;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
public class UserViewController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserViewController(UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{id}")
    public String userProfile(@PathVariable Long id, Model model) {
        UserDto user = userMapper.userToUserDto(userService.getUserById(id));
        if(user == null) return "error/404";

        model.addAttribute("user", user);
        return "account";
    }

    @GetMapping("/{id}/edit")
    public String userProfileEdit(@PathVariable Long id, Model model) {
        UserDto userDto = userMapper.userToUserDto(userService.getUserById(id));
        if(userDto == null) return "error/404";

        model.addAttribute("userDto", userDto);
        return "user-edit";
    }

    @PostMapping("/{id}/edit")
    public String userProfileEditSubmit(@PathVariable Long id,
                                        @ModelAttribute("userDto") UserDto user,
                                        @RequestParam(required = false) String currentPassword,
                                        @RequestParam(required = false) String newPassword,
                                        RedirectAttributes redirectAttributes) {

        User newUser = userMapper.userDtoToUser(user);
        if(newUser == null) return "error/404";

        User updated = userService.updateUserById(id, newUser);

        if (newPassword != null && !newPassword.isEmpty()){
            if (!passwordEncoder.matches(currentPassword, updated.getPassword())){
                redirectAttributes.addFlashAttribute("error", "Passwords do not match");
                return "redirect:/account/" + id + "/edit";
            }

            updated.setPassword(passwordEncoder.encode(newPassword));
            userService.updateUserById(id, updated);
        }

        redirectAttributes.addFlashAttribute("success", "User profile updated");

        return "redirect:/account/" + id;
    }
}
