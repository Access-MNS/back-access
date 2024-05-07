package com.alert.alert.service;

import com.alert.alert.entities.Message;

import java.util.Collection;

public interface MessageDeletedService {

    Collection<Message> getMessagesDeleted();
}
