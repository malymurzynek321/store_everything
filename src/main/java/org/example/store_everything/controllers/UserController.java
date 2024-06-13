package org.example.store_everything.controllers;

import org.example.store_everything.models.Role;
import org.example.store_everything.models.User;
import org.example.store_everything.services.CustomUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {
    private final CustomUserDetailsService userService;

    public UserController(CustomUserDetailsService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String getUsers(Model model) {
        List<User> users = userService.loadAllUsers();
        List<Role> roles = userService.loadAllRoles();

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "admin";
    }

    @GetMapping("/admin/edit")
    public String editUserRedirect() {
        return "redirect:/admin";
    }

    @PostMapping("/admin/edit")
    public String editUserAuthority(@RequestParam String username, @RequestParam String authority) {
        userService.setUserAuthority(username, authority);
        return "redirect:/admin";
    }
}