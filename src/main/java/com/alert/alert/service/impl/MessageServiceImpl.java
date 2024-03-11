package com.alert.alert.service.impl;

import com.alert.alert.entities.Message;
import com.alert.alert.repositories.MessageRepository;
import com.alert.alert.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Optional;

import static com.alert.alert.exceptions.MessageErrors.messageExists;

@Service
public class MessageServiceImpl implements MessageService {

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

        if (!messageExists(messages.getId())) {

            messageRepository.save(messages);
            logger.info("Creating message {}", messages);
            return true;
        }

    return false;
    }

    @Override
    public boolean updateMessage(Message messages) {
        if (messageExists(messages.getId())) {

            logger.info("Updating message {}", messages);
            messageRepository.save(messages);

            return true;
        }

        return false;
    }

    @Override
    public boolean deleteMessage(Long id) {

        if (messageExists(id)) {

            logger.info("Deleting message {}", id);
            messageRepository.deleteById(id);

            return true;
        }

        return false;
    }
}
