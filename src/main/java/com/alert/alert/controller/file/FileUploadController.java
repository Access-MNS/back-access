package com.alert.alert.controller.file;

import com.alert.alert.entity.Views;
import com.alert.alert.service.file.FileUploadService;
import com.alert.alert.validation.IsUser;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
@IsUser
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg");

    @PostMapping(value = "/channel/{channelId}", consumes = "multipart/form-data")
    @JsonView(Views.Public.class)
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @PathVariable Long channelId) throws IOException {

        return fileUploadService.saveFileInDatabase(file, channelId)
                ? "File successfully uploaded"
                : "File upload failed";
    }

    @PostMapping(value = "/user/{userId}", consumes = "multipart/form-data")
    @JsonView(Views.Public.class)
    public String uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                       @PathVariable Long userId) throws IOException {

        if (contentTypes.contains(file.getContentType())) {
            return fileUploadService.saveProfilePictureInDatabase(file, userId)
                    ? "Image successfully uploaded"
                    : "Image upload failed";
        }

        return "File format not supported. Please use png or jpeg images";
    }
}
