package com.alert.alert.controller.message;

import com.alert.alert.entity.Message;
import com.alert.alert.entity.Views;
import com.alert.alert.service.message.MessageDeletedService;
import com.alert.alert.service.impl.message.MessageDeletedServiceImpl;
import com.alert.alert.validation.IsUser;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/message")
@IsUser
public class MessageDeletedController {

    private final MessageDeletedService messageDeletedService;

    @Autowired
    public MessageDeletedController(MessageDeletedServiceImpl messageDeletedService) {
        this.messageDeletedService = messageDeletedService;
    }

    @GetMapping("/deleted")
    @JsonView(Views.Public.class)
    Collection<Message> messagesDeleted() {
        return messageDeletedService.getMessagesDeleted();
    }

}
