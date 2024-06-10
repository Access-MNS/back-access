package com.alert.alert.service;

import com.alert.alert.entities.Channel;

import java.util.Collection;

public interface ChannelService {
    Collection<Channel> getChannels();
    Channel getChannel(Long id);
    Channel createChannel(Channel channel);
    Channel updateChannel(Channel channel);
    boolean deleteChannel(Long id);
    boolean channelExists (Long id);
}
