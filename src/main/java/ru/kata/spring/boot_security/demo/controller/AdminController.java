package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getAllUsers(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("userList", users);
        return "admin/user-list";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/create")
    public String getFormForCreateUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roleList", roleService.allRoles());
        return "admin/create-user"; // Аналогично изменен путь
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user) {
        userService.add(user);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String getFormForUpdateUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roleList", roleService.allRoles());
        return "admin/user-update";
    }

    @PutMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/admin";
    }
}
