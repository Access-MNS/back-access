package com.alert.alert.controller.file;

import com.alert.alert.entity.Document;
import com.alert.alert.service.file.FileDownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/download")
@RequiredArgsConstructor
public class FileDownloadController {

    private final FileDownloadService fileDownloadService;

    @GetMapping("/user/profile_picture/{userId}")
    public ResponseEntity<Resource> downloadProfilePicture(@PathVariable Long userId) {
        Document profilePicture = fileDownloadService.getProfilePicture(userId);

        if (profilePicture != null) {
            ByteArrayResource resource = new ByteArrayResource(profilePicture.getData());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(profilePicture.getType()));
            headers.setContentLength(profilePicture.getData().length);
            headers.setContentDispositionFormData("attachment", profilePicture.getName());

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }
}
