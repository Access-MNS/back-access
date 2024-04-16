package com.alert.alert.service.impl;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.ChannelsUsers;
import com.alert.alert.entities.User;
import com.alert.alert.repositories.ChannelsUsersRepository;
import com.alert.alert.service.ChannelsUsersService;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;

@Service
public class ChannelsUsersServiceImpl implements ChannelsUsersService {

    ChannelsUsersRepository channelsUsersRepository;

    public ChannelsUsersServiceImpl(ChannelsUsersRepository channelsUsersRepository) {
        this.channelsUsersRepository = channelsUsersRepository;
    }

    @Override
    public ChannelsUsers getChannelUser(Long userId, Long channelId) {
        Optional<ChannelsUsers> channelsUsers = channelsUsersRepository.findByUserIdAndChannelId(userId, channelId);
        return channelsUsers
                .orElse(null);
    }

    @Override
    public Set<User> getUsers(Long channelId) {
        return channelsUsersRepository.getChannelsUsersByChannelId(channelId);
    }

    @Override
    public Set<Channel> getChannelsByUserId(Long userId) {
        return channelsUsersRepository.getChannelsByUserId(userId);
    }
}
