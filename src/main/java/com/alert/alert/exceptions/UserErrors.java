package com.alert.alert.exceptions;

import com.alert.alert.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UserErrors {

    private static final Logger logger = LoggerFactory.getLogger(UserErrors.class);
    private static UserRepository userRepository;


    public static boolean userExists(Long id) {
        if(!userRepository.existsById(id)) {
            logger.info("User {} not found", id);
            return false;
        }
        logger.info("User {} already exists", id);
        return true;
    }
}
