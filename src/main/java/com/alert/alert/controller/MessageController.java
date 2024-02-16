package com.alert.alert.controller;

import com.alert.alert.model.Message;
import com.alert.alert.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class MessagesController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private MessageRepository messageRepository;

    public MessagesController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @RequestMapping("/messages")
    Collection<Message> messages() {
        return (Collection<Message>) messageRepository.findAll();
    }

    @RequestMapping("/messages/{id}")
    ResponseEntity<?> getMessage(@PathVariable Long id) {
        Optional<Message> messages = messageRepository.findById(id);
        return messages.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/messages")
    ResponseEntity<Message> createMessage(@Validated @RequestBody Message messages) throws URISyntaxException {
        logger.info("New message : {}", messages);
        Message result = messageRepository.save(messages);
        return ResponseEntity.created(new URI("/api/message" + result.getId()))
              .body(result);
    }

    @PutMapping("messages/{id}")
    ResponseEntity<Message> updateMessage(@Validated @RequestBody Message messages) {
        logger.info("Updating message {}", messages);
        Message result = messageRepository.save(messages);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("messages/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
        logger.info("Deleting message {}", id);
        messageRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}