package com.alert.alert.service.impl;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.ChannelUser;
import com.alert.alert.entities.User;
import com.alert.alert.repositories.ChannelRepository;
import com.alert.alert.repositories.ChannelsUsersRepository;
import com.alert.alert.repositories.UserRepository;
import com.alert.alert.service.ChannelsUsersService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ChannelsUsersServiceImpl implements ChannelsUsersService {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final ChannelServiceImpl channelService;
    private final ChannelRepository channelRepository;
    private final ChannelsUsersRepository channelsUsersRepository;


    public ChannelsUsersServiceImpl(UserServiceImpl userService, UserRepository userRepository, ChannelServiceImpl channelService, ChannelRepository channelRepository, ChannelsUsersRepository channelsUsersRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.channelService = channelService;
        this.channelRepository = channelRepository;
        this.channelsUsersRepository = channelsUsersRepository;
    }

    @Override
    public ChannelUser getChannelUser(Long userId, Long channelId) {
        Optional<ChannelUser> channelsUsers = channelsUsersRepository.findByUserIdAndChannelId(userId, channelId);
        return channelsUsers.orElse(null);
    }

    @Override
    public Set<User> getUsers(Long channelId) {
        return channelsUsersRepository.getChannelsUsersByChannelId(channelId);
    }

    @Override
    public Set<ChannelUser> getUsersChannel(Long channelId) {
        return channelsUsersRepository.findAllByChannelId(channelId);
    }

    @Override
    public Set<Channel> getChannelsByUserId(Long userId) {
        return channelsUsersRepository.getChannelsByUserId(userId);
    }

    @Override
    public boolean addUserToChannel(Long userId, Long channelId) {
        if (channelExists(channelId) && userExists(userId) && !channelUserExists(userId, channelId)) {

            Channel channel = channelService.getChannel(channelId);
            User user = userService.getUser(userId);

            channel.addChannelUser(user, false, false, true, false);
            channelRepository.save(channel);
            return true;
        }
        return false;
    }

    @Override
    public Channel updateUserFromChannel(Long userId, Long channelId,
                                         boolean canEdit, boolean canDelete, boolean canView, boolean canInvite){

        if (channelExists(channelId) && userExists(userId)){

            Channel channel = channelService.getChannel(channelId);
            channel.updateProperties(getChannelUser(userId, channelId),
                    canEdit, canDelete, canView, canInvite);
            return channelRepository.save(channel);
        }
        return null;
    }

    @Override
    public boolean removeUserFromChannel(Long userId, Long channelId) {
        if (channelExists(channelId) && userExists(userId)) {

            Channel channel = channelService.getChannel(channelId);
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

    public boolean channelUserExists(Long userId, Long channelId) {
        return channelsUsersRepository.existsByUserIdAndChannelId(userId, channelId);
    }
}
