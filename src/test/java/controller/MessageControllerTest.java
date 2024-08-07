package controller;

import com.alert.alert.controller.message.MessageController;
import com.alert.alert.entity.Message;
import com.alert.alert.payload.request.MessageRequest;
import com.alert.alert.payload.request.UserRequest;
import com.alert.alert.service.impl.message.MessageServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {

    @Mock
    private MessageServiceImpl messageService;

    @InjectMocks
    private MessageController messageController;

    @Test
    void testGetMessages() {
        List<Message> messages = List.of(new Message(), new Message());
        when(messageService.getMessages()).thenReturn(messages);

        Collection<Message> response = messageController.messages();

        assertEquals(2, response.size());
        verify(messageService, times(1)).getMessages();
    }

    @Test
    void testGetMessage() {
        Message message = new Message();
        when(messageService.getMessage(1L)).thenReturn(message);

        ResponseEntity<Message> response = messageController.getMessage(1L);

        assertEquals(ResponseEntity.ok(message), response);
        verify(messageService, times(1)).getMessage(1L);
    }

    @Test
    void testCreateMessage() {
        Message message = new Message();
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setSender(new UserRequest());

        when(messageService.createMessage(any(Message.class), eq(1L))).thenReturn(message);

        ResponseEntity<Message> response = messageController.createMessage(messageRequest, 1L);

        assertEquals(ResponseEntity.ok(message), response);
        verify(messageService, times(1)).createMessage(any(Message.class), eq(1L));
    }

    @Test
    void testUpdateMessage() throws JsonProcessingException {
        Message message = new Message();
        when(messageService.updateMessage(1L, "New message")).thenReturn(message);

        ResponseEntity<Message> response = messageController.updateMessage(1L, "New message");

        assertEquals(ResponseEntity.ok(message), response);
        verify(messageService, times(1)).updateMessage(1L, "New message");
    }

    @Test
    void testDeleteMessage() throws JsonProcessingException {
        when(messageService.deleteMessage(1L)).thenReturn(true);

        ResponseEntity<Message> response = messageController.deleteMessage(1L);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(messageService, times(1)).deleteMessage(1L);
    }

}
