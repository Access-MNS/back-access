package com.alert.alert.service;

import com.alert.alert.entities.Message;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Collection;

public interface MessageService {
    Collection<Message> getMessages();
    Collection<Message> getMessagesInChannel(Long id);
    Message getMessage(Long id);
    Message createMessage(Message messages, Long channelId);
    Message updateMessage(Long id, String text) throws JsonProcessingException;
    boolean deleteMessage(Long id) throws JsonProcessingException;
}
