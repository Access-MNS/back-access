package com.alert.alert.service;

import com.alert.alert.entities.Message;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

public interface MessageService {
    Collection<Message> getMessages();
    Message getMessage(@PathVariable Long id);
    boolean createMessage(@Validated @RequestBody Message messages);
    boolean updateMessage(@Validated @RequestBody Message messages);
    boolean deleteMessage(@PathVariable Long id);
}
