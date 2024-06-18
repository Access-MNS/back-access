package com.alert.alert.service.impl.file;

import com.alert.alert.entity.Document;
import com.alert.alert.entity.Message;
import com.alert.alert.entity.enums.Action;
import com.alert.alert.repository.ChannelRepository;
import com.alert.alert.repository.DocumentRepository;
import com.alert.alert.repository.UserRepository;
import com.alert.alert.service.channel.ChannelService;
import com.alert.alert.service.file.FileUploadService;
import com.alert.alert.service.message.MessageService;
import com.alert.alert.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

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
        Optional<Document> existingDocumentOptional = documentRepository.findByUserId(userId);

        if (userRepository.existsById(userId)) {
            if (existingDocumentOptional.isPresent()) {
                // Update existing document
                Document existingDocument = existingDocumentOptional.get();
                existingDocument.setName(userId + "u_" + file.getOriginalFilename());  // Optionally update the name if needed
                existingDocument.setType(file.getContentType());
                existingDocument.setData(file.getBytes());
                documentRepository.save(existingDocument);
                log.info("Updating profile picture for user {} in database", userId);
            } else {
                // Save new document
                Document newDocument = new Document(
                        userId + "u_" + file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                );
                newDocument.setUser(userService.getUser(userId));
                documentRepository.save(newDocument);
                log.info("Saving profile picture for user {} in database", userId);
            }
            return true;
        }

        log.info("User {} not found", userId);
        return false;
    }

}
