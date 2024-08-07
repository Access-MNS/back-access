package service;

import com.alert.alert.entity.Channel;
import com.alert.alert.entity.Document;
import com.alert.alert.entity.Message;
import com.alert.alert.entity.User;
import com.alert.alert.repository.ChannelRepository;
import com.alert.alert.repository.DocumentRepository;
import com.alert.alert.repository.UserRepository;
import com.alert.alert.service.channel.ChannelService;
import com.alert.alert.service.impl.file.FileUploadServiceImpl;
import com.alert.alert.service.message.MessageService;
import com.alert.alert.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class FileUploadServiceImplTest {
    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelService channelService;

    @Mock
    private MessageService messageService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FileUploadServiceImpl fileUploadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveFileInDatabaseSuccess() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("file.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenReturn(new byte[0]);
        when(channelRepository.existsById(anyLong())).thenReturn(true);
        when(documentRepository.existsByName(anyString())).thenReturn(false);
        when(channelService.getChannel(anyLong())).thenReturn(mock(Channel.class));
        when(documentRepository.save(any(Document.class))).thenReturn(mock(Document.class));
        when(messageService.createMessage(any(Message.class), anyLong())).thenReturn(mock(Message.class));

        boolean result = fileUploadService.saveFileInDatabase(file, 1L);

        assertTrue(result);
        verify(documentRepository, times(1)).save(any(Document.class));
        verify(messageService, times(1)).createMessage(any(Message.class), anyLong());
    }

    @Test
    void testSaveFileInDatabaseChannelNotFound() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("file.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenReturn(new byte[0]);
        when(channelRepository.existsById(anyLong())).thenReturn(false);

        boolean result = fileUploadService.saveFileInDatabase(file, 1L);

        assertFalse(result);
        verify(documentRepository, never()).save(any(Document.class));
    }

    @Test
    void testSaveFileInDatabaseFileExists() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("file.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenReturn(new byte[0]);
        when(channelRepository.existsById(anyLong())).thenReturn(true);
        when(documentRepository.existsByName(anyString())).thenReturn(true);

        boolean result = fileUploadService.saveFileInDatabase(file, 1L);

        assertFalse(result);
        verify(documentRepository, never()).save(any(Document.class));
    }

    @Test
    void testSaveProfilePictureInDatabaseSaveNew() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("profile.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenReturn(new byte[0]);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(documentRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        when(userService.getUser(anyLong())).thenReturn(mock(User.class));
        when(documentRepository.save(any(Document.class))).thenReturn(mock(Document.class));

        boolean result = fileUploadService.saveProfilePictureInDatabase(file, 1L);

        assertTrue(result);
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    void testSaveProfilePictureInDatabaseUpdateExisting() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("profile.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenReturn(new byte[0]);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(documentRepository.findByUserId(anyLong())).thenReturn(Optional.of(mock(Document.class)));
        when(documentRepository.save(any(Document.class))).thenReturn(mock(Document.class));

        boolean result = fileUploadService.saveProfilePictureInDatabase(file, 1L);

        assertTrue(result);
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    void testSaveProfilePictureInDatabaseUserNotFound() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("profile.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenReturn(new byte[0]);
        when(userRepository.existsById(anyLong())).thenReturn(false);

        boolean result = fileUploadService.saveProfilePictureInDatabase(file, 1L);

        assertFalse(result);
        verify(documentRepository, never()).save(any(Document.class));
    }
}
