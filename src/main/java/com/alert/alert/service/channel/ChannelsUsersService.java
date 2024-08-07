package com.alert.alert.service.channel;

import com.alert.alert.entity.Channel;
import com.alert.alert.entity.ChannelUser;
import com.alert.alert.entity.User;

import java.util.Set;

public interface ChannelsUsersService {

    ChannelUser getChannelUser(Long userId, Long channelId);
    Set<User> getUsers(Long channelId);
    Set<ChannelUser> getUsersChannel(Long channelId);
    Set<Channel> getChannelsByUserId(Long userId);
    boolean addUserToChannel(Long userId, Long channelId);
    Channel updateUserFromChannel(Long userId, Long channelId,
                                  boolean canEdit, boolean canDelete, boolean canView, boolean canInvite);
    boolean removeUserFromChannel(Long userId, Long channelId);
    boolean channelUserExists(Long userId, Long channelId);

}