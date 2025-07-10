package org.practice._3d_prin_shop.view_controller;

import org.practice._3d_prin_shop.dto.UserDto;
import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.service.UserService;
import org.practice._3d_prin_shop.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthorizeController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthorizeController(UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        userService.printAllUsers();
        return "login";
    }

//    @PostMapping("/login")


    @GetMapping("/register")
    public String registerPage(Model model)
    {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto userDto, BindingResult bindingResult) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.userDto", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        User user = userMapper.userDtoToUser(userDto);
        System.err.println(user.getPassword());

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userService.addUser(user);
        return "redirect:/login";
    }
}
