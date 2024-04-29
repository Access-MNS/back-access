package com.alert.alert.service.impl;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.Message;
import com.alert.alert.entities.User;
import com.alert.alert.repositories.ChannelRepository;
import com.alert.alert.repositories.ChannelsUsersRepository;
import com.alert.alert.repositories.UserRepository;
import com.alert.alert.service.ChannelService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final ChannelsUsersRepository channelsUsersRepository;
    private final ChannelsUsersServiceImpl channelsUsersService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    public ChannelServiceImpl(ChannelsUsersRepository channelsUsersRepository, ChannelRepository channelRepository, ChannelsUsersServiceImpl channelsUsersService, SimpMessagingTemplate messagingTemplate, UserRepository userRepository, UserServiceImpl userService) {
        this.channelsUsersRepository = channelsUsersRepository;
        this.channelsUsersService = channelsUsersService;
        this.channelRepository = channelRepository;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public Collection<Channel> getChannels() {
        return channelRepository.findAll();
    }

    @Override
    public Channel getChannel(Long id) {
        return channelRepository.findById(id).orElse(null);
    }

    @Override
    public Channel createChannel(Channel channel) {
        if (!channelExists(channel.getId())
                && (channel.getParentChannelId() == null
                || parentChannelExists(channel.getParentChannelId().getId()))) {

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
        return channelExists(channel.getId())
                ? channelRepository.save(channel)
                : null;
    }

    @Override
    public boolean deleteChannel(Long id) {
        if (channelExists(id)) {
            channelRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean addUserToChannel(Long userId, Long channelId) {
        if (channelExists(channelId) && userExists(userId) && !channelUserExists(userId, channelId)) {

            Channel channel = getChannel(channelId);
            User user = userService.getUser(userId);

            channel.addChannelUser(user, false, false, true, false);
            channelRepository.save(channel);
            //sendMessageToChannel(channelId, new Message(user, Action.JOINED));
            return true;
        }
        return false;
    }

    private void sendMessageToChannel(Long channelId, Message message) {
        messagingTemplate.convertAndSend("/room/" + channelId, message);
    }

    @Override
    public Channel updateUserFromChannel(Long userId, Long channelId,
                                         boolean canEdit, boolean canDelete, boolean canView, boolean canInvite){

        if (channelExists(channelId) && userExists(userId)){

            Channel channel = getChannel(channelId);
            channel.updateProperties(channelsUsersService.getChannelUser(userId, channelId),
                    canEdit, canDelete, canView, canInvite);
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
            return true;
        }
        return false;
    }

    private boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    private boolean channelExists (Long id) {
        return channelRepository.existsById(id);
    }

    private boolean parentChannelExists(Long id) {
        return channelRepository.existsById(id);
    }

    public boolean channelUserExists(Long userId, Long channelId) {
        return channelsUsersRepository.existsByUserIdAndChannelId(userId, channelId);
    }
}
