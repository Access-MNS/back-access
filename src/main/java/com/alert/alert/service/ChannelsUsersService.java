package com.alert.alert.service;

import com.alert.alert.entities.ChannelsUsers;

import java.util.Collection;

public interface ChannelsUsersService {

    Collection<ChannelsUsers> getChannelsUsers();

    ChannelsUsers getChannelUser(Long userId, Long channelId);
}
