package com.alert.alert.service.impl.file;

import com.alert.alert.entity.Document;
import com.alert.alert.repository.DocumentRepository;
import com.alert.alert.service.file.FileDownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileDownloadServiceImpl implements FileDownloadService {

    private final DocumentRepository documentRepository;

    @Override
    public Document getProfilePicture(long userId) {
        return documentRepository.findByUserId(userId)
                .orElse(null);
    }
}
