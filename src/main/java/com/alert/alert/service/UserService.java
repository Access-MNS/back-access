package com.alert.alert.service;

import com.alert.alert.entities.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getUsers();
    User getUser(Long id);
    boolean updateUser(User user);
    boolean deleteUser(Long id);
}
