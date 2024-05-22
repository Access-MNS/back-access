package com.alert.alert.service.impl;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.User;
import com.alert.alert.repositories.ChannelRepository;
import com.alert.alert.repositories.ChannelsUsersRepository;
import com.alert.alert.service.ChannelService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final ChannelsUsersRepository channelsUsersRepository;
    private final ChannelRepository channelRepository;
    private final UserServiceImpl userService;

    public ChannelServiceImpl(ChannelsUsersRepository channelsUsersRepository, ChannelRepository channelRepository, UserServiceImpl userService) {
        this.channelsUsersRepository = channelsUsersRepository;
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    @Override
    public Collection<Channel> getChannels() {
        return channelRepository.findAllWhereIsNotDeleted();
    }

    @Override
    public Channel getChannel(Long id) {
        return channelRepository.findByIdWhereIsNotDeleted(id).orElse(null);
    }

    @Override
    public Channel createChannel(Channel channel) {
        if (!channelExists(channel.getId())
                && (channel.getParentChannelId() == null
                || channelExists(channel.getParentChannelId().getId()))) {

            channel.addChannelUser(userService.getUser(
                    ((User) SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getPrincipal())
                            .getId()
                    ),
                    true, true, true, true);
            return channelRepository.save(channel);
        }
        return null;
    }

    @Override
    public Channel updateChannel(Channel channel) {
        return channelExists(channel.getId()) ? channelRepository.save(channel) : null;
    }

    @Override
    public boolean deleteChannel(Long id) {
        Channel channel = channelRepository.findById(id).orElse(null);
        if (channel != null) {
            channel.setDeleted(true);
            channelRepository.save(channel);
            return true;
        }
        return false;
    }

    private boolean channelExists (Long id) {
        return channelRepository.existsById(id);
    }

    public boolean channelUserExists(Long userId, Long channelId) {
        return channelsUsersRepository.existsByUserIdAndChannelId(userId, channelId);
    }
}
