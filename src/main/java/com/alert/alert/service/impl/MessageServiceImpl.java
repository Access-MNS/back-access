package com.alert.alert.service.impl;

import com.alert.alert.entities.Message;
import com.alert.alert.repository.MessageRepository;
import com.alert.alert.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    private static final String MESSAGE_NOT_FOUND = "Message not found.";
    private static final String MESSAGE_ALREADY_EXISTS = "Message already exists.";

    private final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Collection<Message> getMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message getMessage(Long id) {
        Optional<Message> message = messageRepository.findById(id);
        return message
                .orElse(null);
    }

    @Override
    public boolean createMessage(Message messages) {
        if (messageRepository.existsById(messages.getId())) {
            logger.error(MESSAGE_ALREADY_EXISTS);
            return false;
        } else {
            logger.info("Creating message {}", messages);
            messages.setLastModifiedBy(messages.getSender().getUsername());
            messageRepository.save(messages);
            return true;
        }
    }

    @Override
    public boolean updateMessage(Message messages) {
        if (!messageRepository.existsById(messages.getId())){
            logger.error(MESSAGE_NOT_FOUND);
            return false;
        } else {
            logger.info("Updating message {}", messages);
            messages.setLastModifiedOn(LocalDateTime.now());
            //messages.setLastModifiedBy
            messageRepository.save(messages);
            return true;
        }
    }

    @Override
    public boolean deleteMessage(Long id) {
        if (!messageRepository.existsById(id)){
            logger.info(MESSAGE_NOT_FOUND);
            return false;
        } else {
            logger.info("Deleting message {}", id);
            messageRepository.deleteById(id);
            return true;
        }
    }
}
