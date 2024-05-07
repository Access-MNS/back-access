package com.alert.alert.service.impl;

import com.alert.alert.entities.Message;
import com.alert.alert.entities.User;
import com.alert.alert.repositories.MessageRepository;
import com.alert.alert.service.MessageNotSeenService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageNotSeenServiceImpl implements MessageNotSeenService {

    private final MessageRepository messageRepository;
    private final ChannelServiceImpl channelService;

    public MessageNotSeenServiceImpl(MessageRepository messageRepository, ChannelServiceImpl channelService) {
        this.messageRepository = messageRepository;
        this.channelService = channelService;
    }

    @Override
    public Collection<Message> getMessagesNotSeen(Long userId) {
        return messageRepository.getMessagesNotSeenByUserId(userId);
    }

    @Override
    public boolean deleteMessageNotSeen(Long userId, Long channelId) {
        if (channelService.channelUserExists(userId, channelId)) {

            Collection<Message> messagesNotSeen = getMessagesNotSeen(userId);
            List<Message> messagesInChannel = messagesNotSeen.stream()
                    .filter(msg -> msg.getChannel().getId() == channelId)
                    .toList();

            for (Message message : messagesInChannel) {
                Set<User> notSeenByUser = message.getSentTo().stream()
                        .filter(user -> user.getId() == userId)
                        .collect(Collectors.toSet());

                message.getSentTo().removeAll(notSeenByUser);
                messageRepository.save(message);
            }

            return true;
        }
        return false;
    }
}
