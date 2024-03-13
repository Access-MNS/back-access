package com.alert.alert.service.impl;

import com.alert.alert.entities.User;
import com.alert.alert.repositories.UserRepository;
import com.alert.alert.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<User> getUsers() {

        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {

        return userRepository.findById(id)
                .orElse(null);
    }

    @Override
    public boolean updateUser(User user) {

        if (userExists(user.getId())) {
            userRepository.save(user);
            logger.info("Updating user : {}", user);

            return true;
        }

        return false;
    }

    @Override
    public boolean deleteUser(Long id) {

        if (userExists(id)) {
            userRepository.deleteById(id);
            logger.info("Deleting user {}", id);

            return true;
        }

        return false;
    }

    private boolean userExists(Long id) {
        if(!userRepository.existsById(id)) {
            logger.info("User {} not found", id);
            return false;
        }
        logger.info("User {} already exists", id);
        return true;
    }
}
