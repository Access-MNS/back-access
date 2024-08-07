package com.alert.alert.service.message;

import com.alert.alert.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Collection;

public interface MessageService {
    Collection<Message> getMessages();
    Collection<Message> getMessagesInChannel(Long id);
    Message getMessage(Long id);
    Message createMessage(Message messages, Long channelId);
    Message updateMessage(Long id, String text) throws JsonProcessingException;
    boolean deleteMessage(Long id) throws JsonProcessingException;
    boolean messageExists(Long id);
}