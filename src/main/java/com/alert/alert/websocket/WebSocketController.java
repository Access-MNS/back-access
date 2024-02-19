package com.alert.alert.websocket;

import com.alert.alert.entities.Message;
import com.alert.alert.entities.User;
import com.alert.alert.entities.enums.Action;
import com.alert.alert.service.impl.OnlineUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;


@Controller
public class WebSocketController {

    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final OnlineUserServiceImpl onlineUsersService;

    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate, OnlineUserServiceImpl onlineUsersService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.onlineUsersService = onlineUsersService;
    }

    @MessageMapping("/chat")
    public void handleChatMessage(@Payload Message message, SimpMessageHeaderAccessor headerAccessor){
        System.out.println("Sending message: " + message);
        simpMessagingTemplate.convertAndSend("/topic/all/messages", message);

        if (Action.JOINED.equals(message.getAction())){
            String userDestination = String.format("/topic/%s/messages", message.getSender());
            onlineUsersService.getOnlineUsers().forEach(onlineUser -> {
                Message newMessage = new Message(Action.JOINED, LocalDateTime.now());
                simpMessagingTemplate.convertAndSend(userDestination, newMessage);
            });

            Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("user", message.getId());
            onlineUsersService.addOnlineUser(message.getSender());
        }
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes == null) {
            log.error("Unable to get the user as headerAccessor.getSessionAttributes()");
            return;
        }

        User user = (User) sessionAttributes.get("user");
        if (user == null) { return;}
        onlineUsersService.removeOnlineUser(user);

        Message message = new Message(Action.LEFT, LocalDateTime.now());
        simpMessagingTemplate.convertAndSend("/topic/all/messages", message);
    }
}
