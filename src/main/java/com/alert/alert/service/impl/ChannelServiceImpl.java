package com.alert.alert.service.impl;

import com.alert.alert.entities.Channel;
import com.alert.alert.repository.ChannelRepository;
import com.alert.alert.service.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class ChannelServiceImpl implements ChannelService {

    private static final String CHANNEL_NOT_FOUND = "Channel not found.";
    private static final String CHANNEL_ALREADY_EXISTS = "Channel already exists.";

    private final Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);
    private final ChannelRepository channelRepository;

    public ChannelServiceImpl(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Collection<Channel> getChannels() {
        return channelRepository.findAll();
    }

    @Override
    public Channel getChannel(Long id) {
        Optional<Channel> channel = channelRepository.findById(id);
        return channel
                .orElse(null);
    }

    @Override
    public boolean createChannel(Channel channel) {
        if (channelRepository.existsById(channel.getId())) {
            logger.error(CHANNEL_ALREADY_EXISTS);
            return false;
        } else {
            logger.info("Creating channel {}", channel);
            //channel.setLastModifiedBy;
            channelRepository.save(channel);
            return true;
        }
    }

    @Override
    public boolean updateChannel(Channel channel) {
        if (!channelRepository.existsById(channel.getId())) {
            logger.error(CHANNEL_NOT_FOUND);
            return false;
        } else {
            logger.info("Updating channel {}", channel);
            channel.setLastModifiedOn(LocalDateTime.now());
            //channel.setLastModifiedBy();
            channelRepository.save(channel);
            return true;
        }
    }

    @Override
    public boolean deleteChannel(Long id) {
        if (!channelRepository.existsById(id)) {
            logger.error(CHANNEL_NOT_FOUND);
            return false;
        } else {
            logger.info("Deleting channel {}", id);
            channelRepository.deleteById(id);
            return true;
        }
    }
}
