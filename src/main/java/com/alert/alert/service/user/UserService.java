package com.alert.alert.service.user;

import com.alert.alert.entity.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getUsers();
    User getUser(Long id);
    boolean updateUser(User user);
    boolean deleteUser(Long id);
    boolean userExists(Long id);
}
