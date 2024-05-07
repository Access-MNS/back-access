package com.alert.alert.controller.messages;

import com.alert.alert.entities.Message;
import com.alert.alert.entities.Views;
import com.alert.alert.service.impl.MessageNotSeenServiceImpl;
import com.alert.alert.validation.IsUser;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1")
@IsUser
public class MessageNotSeenController {
    private final MessageNotSeenServiceImpl messageNotSeenService;

    public MessageNotSeenController(MessageNotSeenServiceImpl messageNotSeenService) {
        this.messageNotSeenService = messageNotSeenService;
    }

    @GetMapping("/messages/not_seen/{userId}")
    @JsonView(Views.Public.class)
    Collection<Message> getMessagesNotSeen(@PathVariable Long userId) {
        return messageNotSeenService.getMessagesNotSeen(userId);
    }

    @DeleteMapping("/messages/not_seen/{userId}/{channelId}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Message> deleteMessageNotSeen(@Validated
                                                        @PathVariable Long userId,
                                                        @PathVariable Long channelId) {

        return messageNotSeenService.deleteMessageNotSeen(userId, channelId)
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}
