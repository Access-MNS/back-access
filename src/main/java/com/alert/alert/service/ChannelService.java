package com.alert.alert.service;

import com.alert.alert.entities.Channel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

public interface ChannelService {
    Collection<Channel> getChannels();
    Channel getChannel(@PathVariable Long id);
    boolean createChannel(@Validated @RequestBody Channel channel);

    boolean addUserToChannel(@Validated @PathVariable Long userId, Long channelId);
    public boolean updateUserFromChannel(@Validated
                                         @PathVariable Long userId, Long channelId,
                                         @RequestBody
                                         boolean canEdit, boolean canDelete, boolean canView, boolean canInvite);
    boolean removeUserFromChannel(@Validated @PathVariable Long userId, Long channelId);
    boolean updateChannel(@Validated @RequestBody Channel channel);
    boolean deleteChannel(@PathVariable Long id);
}
