package org.practice._3d_prin_shop.view_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainViewController {

    @GetMapping
    public String index() {
        return "redirect:/products";
    }

    @GetMapping("checkout")
    public String checkout() {
        return "redirect:/";
    }
}
