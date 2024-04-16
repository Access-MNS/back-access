package com.alert.alert.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    boolean saveFileInDatabase(MultipartFile file, Long channelId) throws IOException;
    boolean saveProfilePictureInDatabase(MultipartFile file, long userId) throws IOException;
}
