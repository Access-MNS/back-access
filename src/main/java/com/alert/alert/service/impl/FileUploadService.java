package com.alert.alert.service.impl;

import com.alert.alert.entities.Document;
import com.alert.alert.entities.Message;
import com.alert.alert.entities.enums.Action;
import com.alert.alert.repositories.ChannelRepository;
import com.alert.alert.repositories.DocumentRepository;
import com.alert.alert.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final DocumentRepository documentRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ChannelServiceImpl channelService;
    private final MessageServiceImpl messageService;
    private final UserServiceImpl userService;


    public boolean saveFileInDatabase(MultipartFile file, Long channelId) throws IOException {
        if (channelRepository.existsById(channelId)
            && !documentRepository.existsByName(file.getOriginalFilename())) {

            Document doc = new Document(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            doc.setChannel(channelService.getChannel(channelId));
            documentRepository.save(doc);
            messageService.createMessage(new Message(Action.FILE), channelId);
            log.info("Saving file {} in channel {}", file.getOriginalFilename(), channelId);

            return true;
        }

        log.info("Channel {} not found", channelId);
        return false;
    }

    public boolean saveProfilePictureInDatabase(MultipartFile file, long userId) throws IOException {
        if (userRepository.existsById(userId)
            && !documentRepository.existsByName(file.getOriginalFilename())) {

            Document doc = new Document(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            doc.setUser(userService.getUser(userId));
            documentRepository.save(doc);
            log.info("Saving file {} in database", file.getOriginalFilename());

            return true;
        }

        log.info("User {} not found", userId);
        return false;
    }
}