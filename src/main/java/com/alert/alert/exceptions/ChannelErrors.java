package com.alert.alert.exceptions;

import com.alert.alert.repositories.ChannelRepository;
import com.alert.alert.repositories.ChannelsUsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ChannelErrors {

    private static final Logger logger = LoggerFactory.getLogger(ChannelErrors.class);
    private static ChannelsUsersRepository channelsUsersRepository;
    private static ChannelRepository channelRepository;

    public static boolean channelExists (Long id) {
        if (!channelRepository.existsById(id)) {
            logger.info("Channel {} does not exist", id);
            return false;
        }
        logger.info("Channel {} already exists: ", id);
        return true;
    }

    public static boolean parentChannelExists(Long id) {
        if(!channelRepository.existsById(id)) {
            logger.info("Channel {} does not exist: ", id);
            return false;
        }
        logger.info("Channel {} already exists: ", id);
        return true;
    }

    public static boolean channelUserExists(Long userId, Long channelId) {
        if(!channelsUsersRepository.existsByUserIdAndChannelId(userId, channelId)){
            logger.info("User {} not in channel {}", userId, channelId);
            return false;
        }
        logger.info("User {} already exists in channel {}", userId, channelId);
        return true;
    }
}
