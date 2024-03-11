package com.alert.alert.service;

import com.alert.alert.entities.ChannelsUsers;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;

public interface ChannelsUsersService {

    Collection<ChannelsUsers> getChannelsUsers();

    ChannelsUsers getChannelUser(@PathVariable Long userId, Long channelId);
}
