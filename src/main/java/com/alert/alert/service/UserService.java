package com.alert.alert.service;

import com.alert.alert.entities.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

public interface UserService {
    Collection<User> getUsers();
    User getUser(@PathVariable Long id);
    boolean updateUser(@Validated @RequestBody User user);
    boolean deleteUser(@PathVariable Long id);
}
