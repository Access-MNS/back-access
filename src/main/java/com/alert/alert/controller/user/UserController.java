package com.alert.alert.controller.user;

import com.alert.alert.entity.User;
import com.alert.alert.entity.Views;
import com.alert.alert.payload.request.UserRequest;
import com.alert.alert.service.user.UserService;
import com.alert.alert.service.impl.user.UserServiceImpl;
import com.alert.alert.validation.IsAdmin;
import com.alert.alert.validation.IsUser;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/user")
@IsUser
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    @JsonView(Views.Public.class)
    Collection<User> users() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return user != null
                ? ResponseEntity.ok().body(user)
                : ResponseEntity.notFound().build();
    }

    @PutMapping
    @JsonView(Views.Public.class)
    ResponseEntity<User> updateUser(@Validated @RequestBody UserRequest request) {
        return userService.updateUser(request.toUser())
                ? ResponseEntity.ok(request.toUser())
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    @JsonView(Views.Public.class)
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}