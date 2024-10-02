package ru.kata.spring.bootSecurity.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.bootSecurity.demo.model.User;
import ru.kata.spring.bootSecurity.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/authorise")
    public User getAufUser(@AuthenticationPrincipal User aufUser) {
        return userService.getUserById(aufUser.getId());
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();

    }

    // Создаю пользователя
    @PostMapping()
    public ResponseEntity<HttpStatus> createUser(@RequestBody User user,
                                                 @RequestParam List<Long> roles) {
        System.out.println("Received user: " + user);
        System.out.println("Received roles: " + roles);
        userService.saveUser(user, roles);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> getAllUsers(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(
            @PathVariable Long id,
            @RequestParam String editUserModal_name,
            @RequestParam String editUserModal_userSurname,
            @RequestParam int editUserModal_age,
            @RequestParam String editUserModal_userEmail,
            @RequestParam(required = false) String editUserModal_password,
            @RequestParam List<Long> roles) {

        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }

        // Обновление полей пользователя
        user.setUsername(editUserModal_name);
        user.setUserEmail(editUserModal_userEmail);
        user.setUserSurname(editUserModal_userSurname);
        user.setAge(editUserModal_age);

        userService.updateUser(id, user, editUserModal_password, roles); // Сохраняем обновленного пользователя и идентификаторы ролей

        return ResponseEntity.ok(HttpStatus.OK); // Возвращаем успешный ответ
    }
}