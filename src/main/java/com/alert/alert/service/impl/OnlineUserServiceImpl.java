package com.alert.alert.service.impl;

import com.alert.alert.entities.User;
import com.alert.alert.service.OnlineUserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Service
public class OnlineUserServiceImpl implements OnlineUserService {

    private final Set<User> onlineUsers = new LinkedHashSet<>();

    @Override
    public void addOnlineUser(User user) {
        onlineUsers.add(user);
    }

    @Override
    public void removeOnlineUser(User user) {
        onlineUsers.remove(user);
    }
}
