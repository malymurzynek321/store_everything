package org.example.store_everything.controllers;

import org.example.store_everything.models.User;
import org.example.store_everything.services.CustomUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class RegistrationController {
    private final CustomUserDetailsService userService;

    public RegistrationController(CustomUserDetailsService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        userService.saveUser(user);
        model.addAttribute("successMessage", "Konto zostało poprawnie utworzone.");

        return "login";
    }
}
