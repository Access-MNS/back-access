package com.alert.alert.service;

import com.alert.alert.entities.Message;

import java.util.Collection;

public interface MessageNotSeenService {

    Collection<Message> getMessagesNotSeen(Long id);
    boolean deleteMessageNotSeen(Long userId, Long channelId);
}
