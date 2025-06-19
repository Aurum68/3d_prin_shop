package org.practice._3d_prin_shop.view_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorizeController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
