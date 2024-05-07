package com.alert.alert.service.impl;

import com.alert.alert.entities.Message;
import com.alert.alert.repositories.MessageRepository;
import com.alert.alert.service.MessageDeletedService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MessageDeletedServiceImpl implements MessageDeletedService {

    private final MessageRepository messageRepository;

    public MessageDeletedServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Collection<Message> getMessagesDeleted() {
        return messageRepository.getMessagesByIsDeletedIsTrue();
    }
}
