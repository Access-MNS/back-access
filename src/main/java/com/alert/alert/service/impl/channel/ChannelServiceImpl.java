package com.alert.alert.service.impl.channel;

import com.alert.alert.entity.Channel;
import com.alert.alert.entity.User;
import com.alert.alert.exception.ChannelAlreadyExistsException;
import com.alert.alert.exception.ChannelSaveException;
import com.alert.alert.exception.ParentChannelNotFoundException;
import com.alert.alert.repository.ChannelRepository;
import com.alert.alert.service.channel.ChannelService;
import com.alert.alert.service.impl.user.UserServiceImpl;
import com.alert.alert.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;

    @Autowired
    public ChannelServiceImpl(ChannelRepository channelRepository, UserServiceImpl userService) {
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
        if (channelExists(channel.getId())) {
            throw new ChannelAlreadyExistsException("Le salon existe déjà. ID du salon : " + channel.getId());
        }

        if (channel.getParentChannelId() != null && !channelExists(channel.getParentChannelId().getId())) {
            throw new ParentChannelNotFoundException("Salon parent n'existe pas. ID du salon parent : " + channel.getParentChannelId().getId());
        }

        try {
            channel.addChannelUser(userService.getUser(
                            ((User) SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getPrincipal())
                                    .getId()
                    ),
                    true, true, true, true);
            return channelRepository.save(channel);
        } catch (Exception e) {
            throw new ChannelSaveException("Erreur dans la sauvegarde du salon: " + e.getMessage(), e);
        }
    }

    @Override
    public Channel updateChannel(Long id, Channel channel) {
        channel.setId(id);
        return channelExists(id)
                ? channelRepository.save(channel)
                : null;
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

    @Override
    public boolean channelExists(Long id) {
        return channelRepository.existsById(id);
    }
}
