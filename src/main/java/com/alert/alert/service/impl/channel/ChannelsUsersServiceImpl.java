package com.alert.alert.service.impl.channel;

import com.alert.alert.entity.Channel;
import com.alert.alert.entity.ChannelUser;
import com.alert.alert.entity.User;
import com.alert.alert.repository.ChannelRepository;
import com.alert.alert.repository.ChannelsUsersRepository;
import com.alert.alert.service.channel.ChannelService;
import com.alert.alert.service.channel.ChannelsUsersService;
import com.alert.alert.service.impl.user.UserServiceImpl;
import com.alert.alert.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ChannelsUsersServiceImpl implements ChannelsUsersService {

    private final UserService userService;
    private final ChannelService channelService;
    private final ChannelRepository channelRepository;
    private final ChannelsUsersRepository channelsUsersRepository;

    @Autowired
    public ChannelsUsersServiceImpl(UserServiceImpl userService, ChannelServiceImpl channelService, ChannelRepository channelRepository, ChannelsUsersRepository channelsUsersRepository) {
        this.userService = userService;
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
        return channelsUsersRepository.getChannelsByUserIdWhereIsNotDeleted(userId);
    }

    @Override
    public boolean addUserToChannel(Long userId, Long channelId) {
        if (channelService.channelExists(channelId)
                && userService.userExists(userId)
                && !channelUserExists(userId, channelId)) {

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

        if (channelService.channelExists(channelId)
                && userService.userExists(userId)){

            Channel channel = channelService.getChannel(channelId);
            channel.updateProperties(getChannelUser(userId, channelId),
                    canEdit, canDelete, canView, canInvite);
            return channelRepository.save(channel);
        }
        return null;
    }

    @Override
    public boolean removeUserFromChannel(Long userId, Long channelId) {
        if (channelService.channelExists(channelId) && userService.userExists(userId)) {

            Channel channel = channelService.getChannel(channelId);
            channel.removeChannelUser(userService.getUser(userId));
            channelRepository.save(channel);
            return true;
        }
        return false;
    }

    @Override
    public boolean channelUserExists(Long userId, Long channelId) {
        return channelsUsersRepository.existsByUserIdAndChannelId(userId, channelId);
    }
}
