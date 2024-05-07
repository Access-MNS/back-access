package com.alert.alert.controller.messages;

import com.alert.alert.entities.Message;
import com.alert.alert.entities.Views;
import com.alert.alert.service.impl.MessageDeletedServiceImpl;
import com.alert.alert.validation.IsUser;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1")
@IsUser
public class MessageDeletedController {

    private final MessageDeletedServiceImpl messageDeletedService;

    public MessageDeletedController(MessageDeletedServiceImpl messageDeletedService) {
        this.messageDeletedService = messageDeletedService;
    }

    @GetMapping("/messages/deleted")
    @JsonView(Views.Public.class)
    Collection<Message> messagesDeleted() {
        return messageDeletedService.getMessagesDeleted();
    }

}
