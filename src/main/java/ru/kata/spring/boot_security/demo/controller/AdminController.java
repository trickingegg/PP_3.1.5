package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
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
    public String adminPage(Principal principal, Model model) {
        String username = principal.getName();
        User currentUser = userService.findByUsername(username);
        if(currentUser != null) {
            String fullName = currentUser.getName();
            model.addAttribute("adminName", fullName);
        } else {
            model.addAttribute("adminName", "Undefined");
        }
        return "admin/admin";
    }

    @GetMapping("/userlist")
    public String getAllUsers(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("userList", users);
        return "admin/userList";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin/userList";
    }

    @GetMapping("/createUser")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roleList", roleService.allRoles());
        return "admin/createUser";
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute("user") User user) {
        userService.add(user);
        return "redirect:/admin/userlist";
    }

    @GetMapping("/update/{id}")
    public String getFormForUpdateUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roleList", roleService.allRoles());
        return "admin/updateUser";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/admin/userlist";
    }
}
