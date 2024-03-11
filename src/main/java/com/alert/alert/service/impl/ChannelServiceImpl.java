package com.alert.alert.service.impl;

import static com.alert.alert.exceptions.UserErrors.*;
import static com.alert.alert.exceptions.ChannelErrors.*;
import com.alert.alert.repositories.ChannelRepository;
import com.alert.alert.service.ChannelService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.alert.alert.entities.Channel;
import com.alert.alert.entities.User;
import org.slf4j.LoggerFactory;
import java.util.Collection;
import org.slf4j.Logger;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);
    private final ChannelsUsersServiceImpl channelsUsersService;
    private final ChannelRepository channelRepository;
    private final UserServiceImpl userService;

    public ChannelServiceImpl(ChannelRepository channelRepository, ChannelsUsersServiceImpl channelsUsersService, UserServiceImpl userService) {
        this.channelsUsersService = channelsUsersService;
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    @Override
    public Collection<Channel> getChannels() {

        return channelRepository.findAll();
    }

    @Override
    public Channel getChannel(Long id) {

        return channelRepository.findById(id)
                .orElse(null);
    }

    @Override
    public boolean createChannel(Channel channel) {
        if (!channelExists(channel.getId())
                && (channel.getParentChannelId() == null
                    || parentChannelExists(channel.getParentChannelId().getId()))) {

            channel.addChannelUser(userService.getUser(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()),
                    true, true, true, true);
            channelRepository.save(channel);
            logger.info("Creating channel {}", channel);

            return true;
        }

        return false;
    }

    @Override
    public boolean updateChannel(Channel channel) {
        if (channelExists(channel.getId())){

            channelRepository.save(channel);
            logger.info("Updating channel {}", channel);

            return true;
        }

        return false;
    }

    @Override
    public boolean deleteChannel(Long id) {
        if (channelExists(id)) {

            channelRepository.deleteById(id);
            logger.info("Deleting channel {}", id);
            return true;
        }

        return false;
    }

    @Override
    public boolean addUserToChannel(Long userId, Long channelId) {
        if (channelExists(channelId)
                && userExists(userId)
                && !channelUserExists(userId, channelId)) {

            Channel channel = getChannel(channelId);
            channel.addChannelUser(userService.getUser(userId),
                    false, false, true, false);
            channelRepository.save(channel);
            logger.info("Adding user {} to channel {}", userId, channelId);

            return true;
        }

        return false;
    }

    @Override
    public boolean updateUserFromChannel(Long userId, Long channelId,
                                         boolean canEdit, boolean canDelete, boolean canView, boolean canInvite){

        if (channelExists(channelId) && userExists(userId)){

            Channel channel = getChannel(channelId);
            channel.updateProperties(channelsUsersService.getChannelUser(userId, channelId),
                    canEdit, canDelete, canView, canInvite);
            channelRepository.save(channel);
            logger.info("Adding user {} to channel {}", userId, channelId);

            return true;
        }

        return false;
    }

    @Override
    public boolean removeUserFromChannel(Long userId, Long channelId) {
        if (channelExists(channelId) && userExists(userId)) {

            Channel channel = getChannel(channelId);
            channel.removeChannelUser(userService.getUser(userId));
            channelRepository.save(channel);
            logger.info("Removing user {} to channel {}", userId, channelId);

            return true;
        }

        return false;
    }
}
