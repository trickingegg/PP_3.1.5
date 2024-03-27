package ru.kata.spring.boot_security.demo.start;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class dataInit {
    private final UserService userService;
    private final RoleService roleService;

    public dataInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void initUser() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        roleService.addRole(roleAdmin);
        roleService.addRole(roleUser);

        Set<Role> adminSet = new HashSet<>();
        Set<Role> userSet = new HashSet<>();
        adminSet.add(roleAdmin);
        userSet.add(roleUser);
        User admin = new User("admin", "admin", "Name1", "Surname1", 25, "user1@gmail.com", adminSet);
        User user = new User("user", "user", "Name2", "Surname2", 25, "user2@gmail.com", userSet);

        userService.add(admin);
        userService.add(user);

    }
}