package com.alert.alert.service;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.ChannelsUsers;
import com.alert.alert.entities.User;

import java.util.Set;

public interface ChannelsUsersService {

    ChannelsUsers getChannelUser(Long userId, Long channelId);
    Set<User> getUsers(Long channelId);
    Set<Channel> getChannelsByUserId(Long userId);
}
