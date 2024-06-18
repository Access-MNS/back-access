package com.alert.alert.service.message;

import com.alert.alert.entity.Message;

import java.util.Collection;

public interface MessageDeletedService {

    Collection<Message> getMessagesDeleted();
}
