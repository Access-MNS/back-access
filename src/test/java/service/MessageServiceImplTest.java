package service;

import com.alert.alert.entity.Message;
import com.alert.alert.repository.MessageRepository;
import com.alert.alert.service.impl.message.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetMessages() {
        List<Message> messages = Collections.singletonList(new Message());
        when(messageRepository.findAll()).thenReturn(messages);

        Collection<Message> result = messageService.getMessages();

        assertEquals(messages, result);
        verify(messageRepository, times(1)).findAll();
    }

    @Test
    void testGetMessagesInChannel() {
        Long channelId = 1L;
        List<Message> messages = Collections.singletonList(new Message());
        when(messageRepository.getMessagesByChannel_Id(channelId)).thenReturn(messages);

        Collection<Message> result = messageService.getMessagesInChannel(channelId);

        assertEquals(messages, result);
        verify(messageRepository, times(1)).getMessagesByChannel_Id(channelId);
    }

    @Test
    void testGetMessage() {
        Long messageId = 1L;
        Message message = new Message();
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        Message result = messageService.getMessage(messageId);

        assertEquals(message, result);
        verify(messageRepository, times(1)).findById(messageId);
    }

    @Test
    void testCreateMessageAlreadyExists() {
        Long channelId = 1L;
        Message message = new Message();
        when(messageRepository.existsById(message.getId())).thenReturn(true);

        Message result = messageService.createMessage(message, channelId);

        assertNull(result);
        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    void testDeleteMessageSuccess() {
        Long messageId = 1L;
        Message message = new Message();
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageRepository.existsById(messageId)).thenReturn(true);

        boolean result = messageService.deleteMessage(messageId);

        assertTrue(result);
        assertTrue(message.isDeleted());
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    void testMessageExists() {
        Long messageId = 1L;
        when(messageRepository.existsById(messageId)).thenReturn(true);

        boolean result = messageService.messageExists(messageId);

        assertTrue(result);
        verify(messageRepository, times(1)).existsById(messageId);
    }
}
