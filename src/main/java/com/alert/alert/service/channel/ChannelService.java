package com.alert.alert.service.channel;

import com.alert.alert.entity.Channel;

import java.util.Collection;

public interface ChannelService {
    Collection<Channel> getChannels();
    Channel getChannel(Long id);
    Channel createChannel(Channel channel);
    Channel updateChannel(Long id, Channel channel);
    boolean deleteChannel(Long id);
    boolean channelExists (Long id);
}
