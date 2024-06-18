package com.alert.alert.service.impl.message;

import com.alert.alert.entity.Message;
import com.alert.alert.repository.MessageRepository;
import com.alert.alert.service.message.MessageDeletedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MessageDeletedServiceImpl implements MessageDeletedService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageDeletedServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Collection<Message> getMessagesDeleted() {
        return messageRepository.getMessagesByIsDeletedIsTrue();
    }
}
