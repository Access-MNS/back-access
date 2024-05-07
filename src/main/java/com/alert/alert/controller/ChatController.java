package com.alert.alert.controller;

import com.alert.alert.entities.Message;
import com.alert.alert.service.impl.ChannelsUsersServiceImpl;
import com.alert.alert.service.impl.MessageServiceImpl;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    MessageServiceImpl messageService;
    ChannelsUsersServiceImpl channelService;

    @MessageMapping("/chat/{channelId}/sendMessage")
    public void sendMessage(@Payload Message message, @DestinationVariable("channelId") Long channelId) {
        messageService.createMessage(message, channelId);
    }

    @MessageMapping("/chat/{channelId}/addUser")
    public void addUser(@Payload Message message, @DestinationVariable("channelId") Long channelId) {
        channelService.addUserToChannel(message.getSender().getId(), channelId);
    }
}
