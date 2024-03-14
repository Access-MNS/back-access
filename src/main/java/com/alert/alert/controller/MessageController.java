package com.alert.alert.controller;

import com.alert.alert.entities.Message;
import com.alert.alert.entities.Views;
import com.alert.alert.payload.request.MessageRequest;
import com.alert.alert.service.impl.MessageServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class MessageController {

    private final MessageServiceImpl messageService;

    public MessageController(MessageServiceImpl messageService) {

        this.messageService = messageService;
    }

    @GetMapping("/messages")
    @JsonView(Views.Public.class)
    Collection<Message> messages() {

        return messageService.getMessages();
    }

    @GetMapping("/messages/deleted")
    @JsonView(Views.Public.class)
    Collection<Message> messagesDeleted() {

        return messageService.getMessagesDeleted();
    }

    @GetMapping("/messages/not_seen?{userId}")
    @JsonView(Views.Public.class)
    Collection<Message> messagesNotSeen(@PathVariable Long userId) {

        return messageService.getMessagesNotSeen(userId);
    }

    @GetMapping("/messages?{id}")
    @JsonView(Views.Public.class)
    Collection<Message> getMessages (@PathVariable Long id) {

        return messageService.getMessagesInChannel(id);
    }

    @GetMapping("/message?{id}")
    @JsonView(Views.Public.class)
    ResponseEntity<Message> getMessage(@PathVariable Long id) {

        Message message = messageService.getMessage(id);

        return returnMessage(message);
    }

    @PostMapping("/messages?{channelId}")
    @JsonView(Views.Public.class)
    ResponseEntity<Message> createMessage(@Validated
                                          @RequestBody MessageRequest messageRequest,
                                          @PathVariable Long channelId) {

        Message message = messageService.createMessage(messageRequest.toMessage(), channelId);

        return returnMessage(message);
    }

    @PutMapping("messages")
    @JsonView(Views.Public.class)
    ResponseEntity<Message> updateMessage(@Validated @RequestBody MessageRequest messageRequest) {

        Message message = messageService.updateMessage(messageRequest.toMessage());

        return returnMessage(message);
    }

    @DeleteMapping("messages?{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Message> deleteMessage(@PathVariable Long id) {

        return messageService.deleteMessage(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    private ResponseEntity<Message> returnMessage(Message message) {

        return message != null
                ? ResponseEntity.ok(message)
                : ResponseEntity.notFound().build();
    }
}