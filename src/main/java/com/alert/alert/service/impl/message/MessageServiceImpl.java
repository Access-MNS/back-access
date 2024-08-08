package com.alert.alert.service.impl.message;

import com.alert.alert.entity.Channel;
import com.alert.alert.entity.Message;
import com.alert.alert.entity.User;
import com.alert.alert.exception.ChannelNotFoundException;
import com.alert.alert.exception.MessageSaveException;
import com.alert.alert.exception.UserNotFoundException;
import com.alert.alert.repository.MessageRepository;
import com.alert.alert.service.channel.ChannelService;
import com.alert.alert.service.channel.ChannelsUsersService;
import com.alert.alert.service.message.MessageService;
import com.alert.alert.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MessageServiceImpl implements MessageService {

    private final ChannelsUsersService channelsUsersService;
    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;

    @Autowired
    public MessageServiceImpl(ChannelsUsersService channelsUsersService, MessageRepository messageRepository, ChannelService channelService, UserService userService) {
        this.channelsUsersService = channelsUsersService;
        this.messageRepository = messageRepository;
        this.channelService = channelService;
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
        return messageRepository.findById(id).orElse(null);
    }

    public Message creteMessage(Message message, Long channelId) {
        if (!messageExists(message.getId())) {

            message.setChannel(channelService.getChannel(channelId))
                    .setSender(userService.getUser(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()))
                    .setSentTo(channelsUsersService.getUsers(channelId));
            return messageRepository.save(message);
        }
        return null;
    }

    @Override
    public Message createMessage(Message message, Long channelId) {

        try {
            Channel channel = channelService.getChannel(channelId);
            if (channel == null) {
                throw new ChannelNotFoundException("Aucun salon avec l'id : " + channelId + "n'a été trouvé");
            }

            User senderId = userService.getUser(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

            message.setChannel(channel)
                    .setSender(senderId)
                    .setSentTo(channelsUsersService.getUsers(channelId));

            return messageRepository.save(message);
        } catch (Exception e) {
            throw new MessageSaveException("Erreur dans la sauvegarde du message : " + e.getMessage(), e);
        }
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

    @Override
    public boolean messageExists(Long id) {
        return messageRepository.existsById(id);
    }
}
