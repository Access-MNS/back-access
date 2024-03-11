package com.alert.alert.service;

import com.alert.alert.entities.Message;

import java.util.Collection;

public interface MessageService {
    Collection<Message> getMessages();
    Message getMessage(Long id);
    boolean createMessage(Message messages);
    boolean updateMessage(Message messages);
    boolean deleteMessage(Long id);
}
