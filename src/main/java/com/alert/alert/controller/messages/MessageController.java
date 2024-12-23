package com.alert.alert.controller.messages;

import com.alert.alert.entities.Message;
import com.alert.alert.entities.Views;
import com.alert.alert.entities.enums.PermissionType;
import com.alert.alert.payload.request.MessageRequest;
import com.alert.alert.service.impl.MessageServiceImpl;
import com.alert.alert.validation.PermissionCheck;
import com.alert.alert.validation.IsUser;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1")
@IsUser
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

    @GetMapping("/message/{id}")
    @JsonView(Views.Public.class)
    ResponseEntity<Message> getMessage(@PathVariable Long id) {

        Message message = messageService.getMessage(id);
        return returnMessage(message);
    }

    @GetMapping("/messages/{id}")
    @JsonView(Views.Public.class)
    Collection<Message> getMessagesInChannel(@PermissionCheck(PermissionType.VIEW) @PathVariable Long id) {

        return messageService.getMessagesInChannel(id);
    }

    @PostMapping("/messages/{channelId}")
    @JsonView(Views.Public.class)
    ResponseEntity<Message> createMessage(@Validated
                                          @RequestBody MessageRequest messageRequest,
                                          @PermissionCheck(PermissionType.VIEW) @PathVariable Long channelId) {

        Message message = messageService.createMessage(messageRequest.toMessage(), channelId);
        return returnMessage(message);
    }

    @PutMapping("messages/{messageId}")
    @JsonView(Views.Public.class)
    ResponseEntity<Message> updateMessage(@PathVariable Long messageId,
                                          @RequestBody String messageText) throws JsonProcessingException {

        Message message = messageService.updateMessage(messageId, messageText);
        return returnMessage(message);
    }

    @DeleteMapping("messages/{id}")
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
