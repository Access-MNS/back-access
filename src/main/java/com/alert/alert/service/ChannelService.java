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
    boolean updateChannel(@Validated @RequestBody Channel channel);
    boolean deleteChannel(@PathVariable Long id);
}
