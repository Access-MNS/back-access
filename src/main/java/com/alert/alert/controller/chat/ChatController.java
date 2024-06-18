package com.alert.alert.controller.chat;

import com.alert.alert.entity.Message;
import com.alert.alert.service.channel.ChannelsUsersService;
import com.alert.alert.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    MessageService messageService;
    ChannelsUsersService channelService;

    @Autowired
    public ChatController(MessageService messageService, ChannelsUsersService channelService) {
        this.messageService = messageService;
        this.channelService = channelService;
    }

    @MessageMapping("/chat/{channelId}/sendMessage")
    public void sendMessage(@Payload Message message, @DestinationVariable("channelId") Long channelId) {
        messageService.createMessage(message, channelId);
    }

    @MessageMapping("/chat/{channelId}/addUser")
    public void addUser(@Payload Message message, @DestinationVariable("channelId") Long channelId) {
        channelService.addUserToChannel(message.getSender().getId(), channelId);
    }
}
