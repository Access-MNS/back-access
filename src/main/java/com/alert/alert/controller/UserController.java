package com.alert.alert.controller;

import com.alert.alert.entities.User;
import com.alert.alert.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    Collection<User> users() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return user != null
                ? ResponseEntity.ok().body(user)
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/users")
    ResponseEntity<User> createUser(@Validated @RequestBody User user) {
        return userService.createUser(user)
                ? ResponseEntity.ok(user)
                : ResponseEntity.badRequest().body(user);
    }

    @PutMapping("users")
    ResponseEntity<User> updateUser(@Validated @RequestBody User user) {
        return userService.updateUser(user)
                ? ResponseEntity.ok(user)
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}
