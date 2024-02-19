package com.alert.alert.service;

import com.alert.alert.entities.User;

public interface OnlineUserService {
    void addOnlineUser(User user);
    void removeOnlineUser(User user);
}
