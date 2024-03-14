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
    public Collection<Message> getMessagesDeleted() {
        return messageRepository.getMessagesByIsDeletedIsTrue();
    }

    @Override
    public Collection<Message> getMessagesNotSeen(Long userId) {
        return messageRepository.getMessagesNotSeenByUserId(userId);
    }

    @Override
    public Collection<Message> getMessagesInChannel(Long id) {
        return messageRepository.getMessagesByChannel_Id(id);
    }

    @Override
    public Message getMessage(Long id) {
        return messageRepository.findById(id)
                .orElse(null);
    }

    @Override
    public Message createMessage(Message message, Long channelId) {
        if (!messageExists(message.getId())) {

            message.setChannel(channelService.getChannel(channelId))
                    .setSentTo(channelsUsersService.getUsers(channelId));
            logger.info("Creating message {} in channel {}", message, channelId);

            return messageRepository.save(message);
        }

    return null;
    }

    @Override
    public Message updateMessage(Message messages) {
        return messageExists(messages.getId())
                ? messageRepository.save(messages)
                : null;
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
