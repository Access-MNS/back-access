package com.alert.alert.controller.message;

import com.alert.alert.entity.Message;
import com.alert.alert.entity.Views;
import com.alert.alert.entity.enums.PermissionType;
import com.alert.alert.payload.request.MessageRequest;
import com.alert.alert.service.message.MessageService;
import com.alert.alert.service.impl.message.MessageServiceImpl;
import com.alert.alert.validation.IsUser;
import com.alert.alert.validation.PermissionCheck;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/message")
@IsUser
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageServiceImpl messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @JsonView(Views.Public.class)
    public Collection<Message> messages() {
        return messageService.getMessages();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Message> getMessage(@PathVariable Long id) {

        Message message = messageService.getMessage(id);
        return returnMessage(message);
    }

    @GetMapping("/channel/{id}")
    @JsonView(Views.Public.class)
    Collection<Message> getMessagesInChannel(@PermissionCheck(PermissionType.VIEW) @PathVariable Long id) {

        return messageService.getMessagesInChannel(id);
    }

    @PostMapping("/{channelId}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Message> createMessage(@Validated
                                                 @RequestBody MessageRequest messageRequest,
                                                 @PermissionCheck(PermissionType.VIEW) @PathVariable Long channelId) {

        Message message = messageService.createMessage(messageRequest.toMessage(), channelId);
        return returnMessage(message);
    }

    @PutMapping("/{messageId}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Message> updateMessage(@PathVariable Long messageId,
                                                 @RequestBody String messageText) throws JsonProcessingException {

        Message message = messageService.updateMessage(messageId, messageText);
        return returnMessage(message);
    }

    @DeleteMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Message> deleteMessage(@PathVariable Long id) throws JsonProcessingException {

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
