package com.alert.alert.service;

import com.alert.alert.entities.Message;

import java.util.Collection;

public interface MessageService {
    Collection<Message> getMessages();

    Collection<Message> getMessagesDeleted();

    Collection<Message> getMessagesNotSeen(Long id);

    Collection<Message> getMessagesInChannel(Long id);
    Message getMessage(Long id);
    Message createMessage(Message messages, Long channelId);
    Message updateMessage(Message messages);
    boolean deleteMessage(Long id);
}
