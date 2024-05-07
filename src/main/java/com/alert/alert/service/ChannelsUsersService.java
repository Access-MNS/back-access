package com.alert.alert.service;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.ChannelUser;
import com.alert.alert.entities.User;

import java.util.Set;

public interface ChannelsUsersService {

    ChannelUser getChannelUser(Long userId, Long channelId);
    Set<User> getUsers(Long channelId);
    Set<Channel> getChannelsByUserId(Long userId);
    boolean addUserToChannel(Long userId, Long channelId);

    Channel updateUserFromChannel(Long userId, Long channelId,
                                  boolean canEdit, boolean canDelete, boolean canView, boolean canInvite);

    boolean removeUserFromChannel(Long userId, Long channelId);

}
