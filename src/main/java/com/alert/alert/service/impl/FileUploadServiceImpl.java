package com.alert.alert.service.impl;

import com.alert.alert.entities.Document;
import com.alert.alert.entities.Message;
import com.alert.alert.entities.enums.Action;
import com.alert.alert.repositories.ChannelRepository;
import com.alert.alert.repositories.DocumentRepository;
import com.alert.alert.repositories.UserRepository;
import com.alert.alert.service.ChannelService;
import com.alert.alert.service.FileUploadService;
import com.alert.alert.service.MessageService;
import com.alert.alert.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final DocumentRepository documentRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ChannelService channelService;
    private final MessageService messageService;
    private final UserService userService;

    @Override
    public boolean saveFileInDatabase(MultipartFile file, Long channelId) throws IOException {
        if (channelRepository.existsById(channelId)
            && !documentRepository.existsByName(channelId + "c_" + file.getOriginalFilename())) {

            Document doc = new Document(
                    channelId + "c_" + file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            doc.setChannel(channelService.getChannel(channelId));
            documentRepository.save(doc);
            messageService.createMessage(new Message(Action.FILE), channelId);
            log.info("Saving file {} in channel {}", file.getOriginalFilename(), channelId);

            return true;
        }

        log.info(documentRepository.existsByName(channelId + "c_" + file.getOriginalFilename())
                ? "File already exists in database"
                : "Channel {} not found", channelId);
        return false;
    }

    @Override
    public boolean saveProfilePictureInDatabase(MultipartFile file, long userId) throws IOException {
        if (userRepository.existsById(userId)
            && !documentRepository.existsByName(userId + "u_" + file.getOriginalFilename())) {

            Document doc = new Document(
                    userId + "u_" + file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            doc.setUser(userService.getUser(userId));
            documentRepository.save(doc);
            log.info("Saving file {} in database", file.getOriginalFilename());

            return true;
        }
        log.info(documentRepository.existsByName(userId + "u_" + file.getOriginalFilename())
                ? "File already exists in database"
                : "User {} not found", userId);
        return false;
    }
}
