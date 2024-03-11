package com.alert.alert.service;

import com.alert.alert.entities.Channel;

import java.util.Collection;

public interface ChannelService {
    Collection<Channel> getChannels();
    Channel getChannel(Long id);
    boolean createChannel(Channel channel);

    boolean addUserToChannel(Long userId, Long channelId);
    boolean updateUserFromChannel(Long userId, Long channelId,
                                         boolean canEdit, boolean canDelete, boolean canView, boolean canInvite);
    boolean removeUserFromChannel(Long userId, Long channelId);
    boolean updateChannel(Channel channel);
    boolean deleteChannel(Long id);
}
