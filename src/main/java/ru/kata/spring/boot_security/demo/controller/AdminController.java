package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.*;


@Controller
public class AdminController {


    private final RoleService roleService;

    private final UserService userService;


    public AdminController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;

    }

    @GetMapping(value = "/admin")
    public String listUsers(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        return "users";
    }


    @GetMapping("/admin/user/{id}")
    public String getUserById(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "oneUser";
    }

    @GetMapping(value = "/admin/new")
    public String newPersonForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    @PostMapping
    public String addUser(@ModelAttribute("user") User user) {
        try {
            userService.createUser(user);
        } catch (Exception e) {
            //ignored
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "edit";
    }

    @PatchMapping("/admin/user/{id}")
    public String editUser(@ModelAttribute("user") User user, @RequestParam("allRoles") String[] roles) {

            userService.update(user,roleService.findAll(roles));

        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.delete(userService.getUserById(id));
        return "redirect:/admin";
    }
}
