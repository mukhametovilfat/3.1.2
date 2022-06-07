package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.*;


@Controller
public class UserController {

    private final RoleService roleService;

    private final UserService userService;


    public UserController(RoleService roleService, UserService userService, Set<Role> roleSet) {
        this.roleService = roleService;
        this.userService = userService;

    }

    @GetMapping(value = "/admin")
    public String listUsers(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/user")
    public String getUserById(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByUsername(principal.getName()));
        return "user";
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
    public String editUserForm(@PathVariable("id") long id, Model model) {
        User user = userService.getUserById(id);
        List<Role> allRoles = roleService.findAll();
        Set<Role> set = user.getRoleSet();
        List<Role> list = new ArrayList<>();
        for (Role role : set) {
            list.add(role);
        }
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("userRoles", list);
        return "edit";
    }

    @PatchMapping ("/admin/user/{id}")
    public String editUser(@ModelAttribute("user") User user, @RequestParam("allRoles") String[] roles) {

        Set<Role> userRoles = new HashSet<>();
        for (String id : roles){
        userRoles.add(roleService.getRoleByName(id));
        userService.update(user,userRoles);}

        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.delete(userService.getUserById(id));
        return "redirect:/admin";
    }
}
