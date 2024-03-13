package com.alert.alert.service.impl;

import com.alert.alert.entities.Message;
import com.alert.alert.repositories.MessageRepository;
import com.alert.alert.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MessageServiceImpl implements MessageService {

    private final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final ChannelServiceImpl channelService;
    private final ChannelsUsersServiceImpl channelsUsersService;
    private final MessageRepository messageRepository;

    public MessageServiceImpl(ChannelServiceImpl channelService, ChannelsUsersServiceImpl channelsUsersService, MessageRepository messageRepository) {
        this.channelService = channelService;
        this.channelsUsersService = channelsUsersService;
        this.messageRepository = messageRepository;
    }

    @Override
    public Collection<Message> getMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message getMessage(Long id) {
        return messageRepository.findById(id)
                .orElse(null);
    }

    @Override
    public boolean createMessage(Message message, Long channelId) {

        if (!messageExists(message.getId())) {

            message.setChannel(channelService.getChannel(channelId))
                    .setSentTo(channelsUsersService.getUsers(channelId));
            messageRepository.save(message);
            logger.info("Creating message {} in channel {}", message, channelId);
            return true;
        }

    return false;
    }

    @Override
    public boolean updateMessage(Message messages) {
        if (messageExists(messages.getId())) {

            logger.info("Updating message {}", messages);
            messageRepository.save(messages);

            return true;
        }

        return false;
    }

    @Override
    public boolean deleteMessage(Long id) {
        if (messageExists(id)) {

            Message message = getMessage(id);
            message
                    .setChannel(message.getChannel())
                    .setSender(message.getSender())
                    .setAction(message.getAction())
                    .setComment(message.getComment())
                    .setDeleted(true)
                    .setSentTo(null);
            updateMessage(message);
            logger.info("Message {} now marked as deleted", id);

            return true;
        }

        return false;
    }

    private boolean messageExists(Long id) {
        if (!messageRepository.existsById(id)) {
            logger.error("Message not found.");
            return false;
        }
        logger.error("Message already exists.");

        return true;
    }
}
