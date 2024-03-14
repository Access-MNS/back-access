package com.alert.alert.service.impl;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.User;
import com.alert.alert.repositories.ChannelRepository;
import com.alert.alert.repositories.ChannelsUsersRepository;
import com.alert.alert.repositories.UserRepository;
import com.alert.alert.service.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);
    private final ChannelsUsersRepository channelsUsersRepository;
    private final ChannelsUsersServiceImpl channelsUsersService;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    public ChannelServiceImpl(ChannelsUsersRepository channelsUsersRepository, ChannelRepository channelRepository, ChannelsUsersServiceImpl channelsUsersService, UserRepository userRepository, UserServiceImpl userService) {
        this.channelsUsersRepository = channelsUsersRepository;
        this.channelsUsersService = channelsUsersService;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
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
    public Channel createChannel(Channel channel) {
        if (!channelExists(channel.getId())
                && (channel.getParentChannelId() == null
                    || parentChannelExists(channel.getParentChannelId().getId()))) {

            channel.addChannelUser(userService.getUser(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()),
                    true, true, true, true);
            logger.info("Creating channel {}", channel);

            return channelRepository.save(channel);
        }
        return null;
    }

    @Override
    public Channel updateChannel(Channel channel) {
        return channelExists(channel.getId())
                ? channelRepository.save(channel)
                : null;
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
    public Channel updateUserFromChannel(Long userId, Long channelId,
                                         boolean canEdit, boolean canDelete, boolean canView, boolean canInvite){

        if (channelExists(channelId) && userExists(userId)){

            Channel channel = getChannel(channelId);
            channel.updateProperties(channelsUsersService.getChannelUser(userId, channelId),
                    canEdit, canDelete, canView, canInvite);

            logger.info("Adding user {} to channel {}", userId, channelId);
            return channelRepository.save(channel);
        }

        return null;
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

    private boolean channelExists (Long id) {
        if (!channelRepository.existsById(id)) {
            logger.info("Channel {} does not exist", id);
            return false;
        }
        logger.info("Channel {} already exists: ", id);
        return true;
    }

    private boolean parentChannelExists(Long id) {
        if(!channelRepository.existsById(id)) {
            logger.info("Channel {} does not exist: ", id);
            return false;
        }
        logger.info("Channel {} already exists: ", id);
        return true;
    }

    private boolean channelUserExists(Long userId, Long channelId) {
        if(!channelsUsersRepository.existsByUserIdAndChannelId(userId, channelId)){
            logger.info("User {} not in channel {}", userId, channelId);
            return false;
        }
        logger.info("User {} already exists in channel {}", userId, channelId);
        return true;
    }

    private boolean userExists(Long id) {
        if(!userRepository.existsById(id)) {
            logger.info("User {} not found", id);
            return false;
        }
        logger.info("User {} already exists", id);
        return true;
    }
}
