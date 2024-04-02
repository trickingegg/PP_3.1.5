package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.exceptions.DuplicateUsernameException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<User> users = userService.listUsers();
        model.addAttribute("user", new User());
        model.addAttribute("userList", users);
        model.addAttribute("roleList", roleService.allRoles());
        if(currentUser != null) {
            model.addAttribute("admin", currentUser);
        } else {
            model.addAttribute("adminName", "Undefined");
        }
        return "admin/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam("role") List<String> roleName,
                             RedirectAttributes redirectAttributes) {
        try {
            Set<Role> roles = new HashSet<>();
            for (String roleNames : roleName) {
                roles.add(roleService.findByName(roleNames));
            }
            user.setRoles(roles);
            userService.add(user);
        } catch (DuplicateUsernameException ex) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("roleList", roleService.allRoles());
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("activeTab", "addUserTab");
            return "redirect:/admin";
        }
        return "redirect:/admin";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @ModelAttribute("user") User user,
                             @RequestParam("role") List<String> roleName,
                             RedirectAttributes redirectAttributes) {
        try {
            Set<Role> roles = new HashSet<>();
            for (String roleNames : roleName) {
                roles.add(roleService.findByName(roleNames));
            }
            user.setRoles(roles);
            userService.update(user);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении пользователя: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("errorUserId", id);
            return "redirect:/admin";
        }
        return "redirect:/admin";
    }
}
