package com.alert.alert.service.impl;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.User;
import com.alert.alert.repositories.ChannelRepository;
import com.alert.alert.service.ChannelPrivateService;
import com.alert.alert.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ChannelPrivateServiceImpl implements ChannelPrivateService {

    private final ChannelRepository channelRepository;
    private final UserService userService;

    @Autowired
    public ChannelPrivateServiceImpl(ChannelRepository channelRepository, UserServiceImpl userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    @Override
    public Channel createPrivateChannel(Channel channel, Long id) {
        if (!channelExists(channel.getId())
                && (channel.getParentChannelId() == null
                || channelExists(channel.getParentChannelId().getId()))) {

            channel.setIsPrivate(true);

            channel.addChannelUser(userService.getUser(
                            ((User) SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getPrincipal())
                                    .getId()
                    ),
                    true, true, true, false);
            channel.addChannelUser(userService.getUser(id),
                    true, true, true, false);

            return channelRepository.save(channel);
        }
        return null;
    }

    private boolean channelExists (Long id) {
        return channelRepository.existsById(id);
    }
}
