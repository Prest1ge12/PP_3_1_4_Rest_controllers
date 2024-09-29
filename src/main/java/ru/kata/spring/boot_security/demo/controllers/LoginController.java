package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String index() {
        return "/login/index";
    }

    @GetMapping("/")
    public String redirectToIndex() {
        return "redirect:/login";
    }
}


