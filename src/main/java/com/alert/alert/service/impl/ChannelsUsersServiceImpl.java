package com.alert.alert.service.impl;

import com.alert.alert.entities.ChannelsUsers;
import com.alert.alert.repositories.ChannelsUsersRepository;
import com.alert.alert.service.ChannelsUsersService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ChannelsUsersServiceImpl implements ChannelsUsersService {

    ChannelsUsersRepository channelsUsersRepository;

    public ChannelsUsersServiceImpl(ChannelsUsersRepository channelsUsersRepository) {
        this.channelsUsersRepository = channelsUsersRepository;
    }

    @Override
    public Collection<ChannelsUsers> getChannelsUsers() {
        return channelsUsersRepository.findAll();
    }

    @Override
    public ChannelsUsers getChannelUser(Long userId, Long channelId) {
        Optional<ChannelsUsers> channelsUsers = channelsUsersRepository.findByUserIdAndChannelId(userId, channelId);
        return channelsUsers
                .orElse(null);
    }
}
