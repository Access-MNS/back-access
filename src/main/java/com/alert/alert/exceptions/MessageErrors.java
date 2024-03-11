package com.alert.alert.exceptions;

import com.alert.alert.repositories.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MessageErrors {

    private static final String MESSAGE_NOT_FOUND = "Message not found.";
    public static final String MESSAGE_ALREADY_EXISTS = "Message already exists.";


    private static final Logger logger = LoggerFactory.getLogger(MessageErrors.class);
    private static MessageRepository messageRepository;

    public static boolean messageExists(Long id) {
        if (!messageRepository.existsById(id)) {
            logger.error(MESSAGE_NOT_FOUND);
            return false;
        }
        logger.error(MESSAGE_ALREADY_EXISTS);

        return true;
    }
}
