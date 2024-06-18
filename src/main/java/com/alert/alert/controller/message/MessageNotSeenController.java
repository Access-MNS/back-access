package com.alert.alert.controller.message;

import com.alert.alert.entity.Message;
import com.alert.alert.entity.Views;
import com.alert.alert.service.message.MessageNotSeenService;
import com.alert.alert.service.impl.message.MessageNotSeenServiceImpl;
import com.alert.alert.validation.IsUser;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/message/not_seen")
@IsUser
public class MessageNotSeenController {
    private final MessageNotSeenService messageNotSeenService;

    @Autowired
    public MessageNotSeenController(MessageNotSeenServiceImpl messageNotSeenService) {
        this.messageNotSeenService = messageNotSeenService;
    }

    @GetMapping("/{userId}")
    @JsonView(Views.Public.class)
    Collection<Message> getMessagesNotSeen(@PathVariable Long userId) {
        return messageNotSeenService.getMessagesNotSeen(userId);
    }

    @DeleteMapping("/{userId}/{channelId}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Message> deleteMessageNotSeen(@Validated
                                                        @PathVariable Long userId,
                                                        @PathVariable Long channelId) {

        return messageNotSeenService.deleteMessageNotSeen(userId, channelId)
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}
