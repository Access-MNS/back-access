package com.alert.alert.service.impl;

import com.alert.alert.entities.Message;
import com.alert.alert.entities.User;
import com.alert.alert.repositories.MessageRepository;
import com.alert.alert.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MessageServiceImpl implements MessageService {

    private final ChannelServiceImpl channelService;
    private final ChannelsUsersServiceImpl channelsUsersService;
    private final MessageRepository messageRepository;
    private final UserServiceImpl userService;

    public MessageServiceImpl(ChannelServiceImpl channelService, ChannelsUsersServiceImpl channelsUsersService, MessageRepository messageRepository, UserServiceImpl userService) {
        this.channelService = channelService;
        this.channelsUsersService = channelsUsersService;
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @Override
    public Collection<Message> getMessages() {
        return messageRepository.findAll();
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
                    .setSender(userService.getUser(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()))
                    .setSentTo(channelsUsersService.getUsers(channelId));
            return messageRepository.save(message);
        }
        return null;
    }

    @Override
    public Message updateMessage(Long id, String text) throws JsonProcessingException {
        if (messageExists(id)) {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(text);

            Message message = getMessage(id);
            message.setComment(jsonNode.get("message").asText());

            return messageRepository.save(message);
        }
        return null;
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
                    .setSentTo(null)
                    .setDeleted(true);

            messageRepository.save(message);
            return true;
        }
        return false;
    }

    private boolean messageExists(Long id) {
        return messageRepository.existsById(id);
    }
}
