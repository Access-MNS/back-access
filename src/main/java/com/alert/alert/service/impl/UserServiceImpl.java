package com.alert.alert.service.impl;

import com.alert.alert.entities.User;
import com.alert.alert.security.model.enums.Role;
import com.alert.alert.repository.UserRepository;
import com.alert.alert.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String MAIL_ALREADY_EXISTS = "Email address already exists";

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
        Optional<User> user = userRepository.findById(id);
        return user
                .orElse(null);
    }

    @Override
    public boolean createUser(User user) {
        if(Boolean.TRUE.equals(userRepository.existsByMail(user.getMail()))) {
            logger.error(MAIL_ALREADY_EXISTS);
            return false;
        } else {
            logger.info("Creating user : {}", user);
            user.setCreatedBy(user.getUsername());
            user.setLastModifiedBy(user.getUsername());
            if (user.getRole() == null) {
                user.setRole(Role.USER);
            }
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public boolean updateUser(User user) {
        if(!userRepository.existsById(user.getId())){
            logger.error(USER_NOT_FOUND);
            return false;
        } else {
            logger.info("Updating user : {}", user);
            user.setLastModifiedOn(LocalDateTime.now());
            //user.setLastModifiedBy(user.getUsername());
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)){
            logger.error(USER_NOT_FOUND);
            return false;
        } else {
            logger.info("Deleting user {}", id);
            userRepository.deleteById(id);
            return true;
        }
    }
}
