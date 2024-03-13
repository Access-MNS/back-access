package com.alert.alert.controller;

import com.alert.alert.entities.Message;
import com.alert.alert.service.impl.MessageServiceImpl;
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
    Collection<Message> messages() {
        return messageService.getMessages();
    }

    @GetMapping("/messages/{id}")
    ResponseEntity<Message> getMessage(@PathVariable Long id) {
        Message message = messageService.getMessage(id);
        return message != null
                ? ResponseEntity.ok(message)
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/messages/{channelId}")
    ResponseEntity<Message> createMessage(@Validated @RequestBody Message message, @PathVariable Long channelId) {
        return messageService.createMessage(message, channelId)
                ? ResponseEntity.ok(message)
                : ResponseEntity.badRequest().build();
    }

    @PutMapping("messages")
    ResponseEntity<Message> updateMessage(@Validated @RequestBody Message messages) {
        return messageService.updateMessage(messages)
                ? ResponseEntity.ok(messages)
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("messages/{id}")
    public ResponseEntity<Message> deleteMessage(@PathVariable Long id) {
        return messageService.deleteMessage(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}