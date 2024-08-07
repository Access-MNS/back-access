package controller;

import com.alert.alert.controller.file.FileUploadController;
import com.alert.alert.service.impl.file.FileUploadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileUploadControllerTest {

    @Mock
    private FileUploadServiceImpl fileUploadService;

    @InjectMocks
    private FileUploadController fileUploadController;

    @BeforeEach
    void setUp() {
        fileUploadController = new FileUploadController(fileUploadService);
    }

    @Test
    void testUploadFileSuccess() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(fileUploadService.saveFileInDatabase(any(MultipartFile.class), anyLong())).thenReturn(true);

        String response = fileUploadController.uploadFile(file, 1L);

        assertEquals("File successfully uploaded", response);
        verify(fileUploadService, times(1)).saveFileInDatabase(file, 1L);
    }

    @Test
    void testUploadFileFailure() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(fileUploadService.saveFileInDatabase(any(MultipartFile.class), anyLong())).thenReturn(false);

        String response = fileUploadController.uploadFile(file, 1L);

        assertEquals("File upload failed", response);
        verify(fileUploadService, times(1)).saveFileInDatabase(file, 1L);
    }

    @Test
    void testUploadProfilePictureSuccess() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/png");
        when(fileUploadService.saveProfilePictureInDatabase(any(MultipartFile.class), anyLong())).thenReturn(true);

        String response = fileUploadController.uploadProfilePicture(file, 1L);

        assertEquals("Image successfully uploaded", response);
        verify(fileUploadService, times(1)).saveProfilePictureInDatabase(file, 1L);
    }

    @Test
    void testUploadProfilePictureFailure() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/png");
        when(fileUploadService.saveProfilePictureInDatabase(any(MultipartFile.class), anyLong())).thenReturn(false);

        String response = fileUploadController.uploadProfilePicture(file, 1L);

        assertEquals("Image upload failed", response);
        verify(fileUploadService, times(1)).saveProfilePictureInDatabase(file, 1L);
    }

    @Test
    void testUploadProfilePictureUnsupportedFormat() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");

        String response = fileUploadController.uploadProfilePicture(file, 1L);

        assertEquals("File format not supported. Please use png or jpeg images", response);
        verify(fileUploadService, times(0)).saveProfilePictureInDatabase(any(MultipartFile.class), anyLong());
    }
}

